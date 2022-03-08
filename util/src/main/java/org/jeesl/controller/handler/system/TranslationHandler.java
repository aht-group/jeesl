package org.jeesl.controller.handler.system;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.bean.JeeslLabelBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslationHandler<L extends JeeslLang,D extends JeeslDescription,
								RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
								RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
								RML extends JeeslRevisionMissingLabel>
	implements Serializable,JeeslLabelBean<RE>
{
	final static Logger logger = LoggerFactory.getLogger(TranslationHandler.class);
	private static final long serialVersionUID = 1L;

	private JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?,RML> fRevision;

	private final Class<RE> cRE;
	private final Class<L> cL;

	private final Map<String,Map<String,L>> entities; public Map<String,Map<String,L>> getEntities() {return entities;}
	private final Map<String,Map<String,Map<String,L>>> labels; public Map<String, Map<String, Map<String,L>>> getLabels() {return labels;}
	private final Map<String,Map<String,Map<String,D>>> descriptions; public Map<String, Map<String, Map<String,D>>> getDescriptions() {return descriptions;}
	private JeeslMissingLabelsHandler<L,D,RE,RA,RML> missingLabelHandler; public JeeslMissingLabelsHandler<L,D,RE,RA,RML> getMissingLabelHandler(){return missingLabelHandler;}


	public String entityJscn;

	public TranslationHandler(JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?,RML> fRevision,
			final Class<RE> cRE, final Class<L> cL, final Class<RML> cRml)
	{
		this.fRevision=fRevision;
		this.cRE = cRE;
		this.cL = cL;
		missingLabelHandler = new JeeslMissingLabelsHandler<>(fRevision, cRE, cL, cRml);

		entityJscn ="";

		/**
		 * Customised entities hashMap (override get function)
		 */
        entities = new HashMap<String,Map<String,L>>()
        {
			//private static final long serialVersionUID = 1L;
			/**
			 * Override default get function
			 * so that we can load revision entity (Load translation for entity) on demand
			 * with key is java simple class name of revision entity
			 */
			@Override public Map<String, L> get(Object key)
			{
				//search entities map with key is java simple class name of revision entity
				Map<String,L> m = super.get(key);
				entityJscn = (String)key;

				try
				{
					if(Objects.isNull(m) && !isLoadedRevisionEntity(entityJscn))
					{
						logger.info("searching" + entityJscn);
						RE re =  fRevision.fRevisionEntity(key.toString());
						load(re);
					}
					//search again after loading revision entity
					m = super.get(key);
					if (Objects.nonNull(m))
					{
						missingLabelHandler.checkMissingTranslationInMap(m, entityJscn,"");
						return m;
					}
				}
				catch (JeeslNotFoundException e)  {missingLabelHandler.updateMissingRevisionEntity(entityJscn);}
				catch (AbstractMethodError e)
				{
					logger.info("check if you have duplication of revision facade, for example fRevisionEntity methode define 2 or more times in project");
					missingLabelHandler.updateMissingRevisionEntity(entityJscn);
				}
				return getLangMap(entityJscn,null);
			}
        };

        /**
		 * Customised labels hashMap  (override get function)
		 */
        labels = new HashMap<String,Map<String,Map<String,L>>>()
        {
        	private static final long serialVersionUID = 1L;

			@Override
			public  Map<String, Map<String, L>> get(Object key)
			{
				//search label map with  entity key is java simple class name of revision entity
				Map<String, Map<String, L>> m = super.get(key);
				entityJscn = (String)key;
				try
				{
					if(Objects.isNull(m) && !isLoadedRevisionEntity(entityJscn))
					{
						RE re =  fRevision.fRevisionEntity(entityJscn);
						load(re);
					}
					m = super.get(key);
					if (Objects.nonNull(m))
					{
				        return m;
				    }
				}
				catch(JeeslNotFoundException e) {missingLabelHandler.updateMissingRevisionEntity(entityJscn);}
				return getTempLabelHashtable();
			}
        };

        descriptions = new HashMap<String,Map<String,Map<String,D>>>();
	}
/*
	public void reloadFromDb()
	{
		List<RE> list = fRevision.all(cRE);
        logger.info("building "+list.size());

		for(RE re : list)
		{
			load(re);
		}
	}
*/
	@Override public void reload(RE re)
	{
		try
		{
			Class<?> c = Class.forName(re.getCode());
			if(entities.containsKey(c.getSimpleName())) {entities.remove(c.getSimpleName());}
			if(labels.containsKey(c.getSimpleName())) {labels.remove(c.getSimpleName());}
			if(descriptions.containsKey(c.getSimpleName())) {descriptions.remove(c.getSimpleName());}
			missingLabelHandler.reload(re);
			load(re);
		}
		catch (ClassNotFoundException e) {logger.warn("CNFE: "+re.getCode());}
	}
/**
 * Load Revision entity
 * load revision entity should be only done once
 * Attempt to load revision entity which do not have translation should only be done once
 * so miss list of revision entities should be maintained so no retry for missing translation should be done.
 */
	@SuppressWarnings("rawtypes")
	public void load(RE re)
	{
		try
		{
			Class<?> c = Class.forName(re.getCode());

			re = fRevision.load(cRE, re);
			missingLabelHandler.load(re);
			if(entities.containsKey(c.getSimpleName())){logger.warn("Duplicate classs in Revisions "+re.getCode());}

			entities.put(c.getSimpleName(), re.getName());

			labels.put(c.getSimpleName(),getTempLabelHashtable());
			descriptions.put(c.getSimpleName(), new Hashtable<String,Map<String,D>>());

			//Prepare a list of attributes from "Attributes" or "Labels" Enum
			List<Class> classes = new ArrayList<>();
			List<Field> fields = new ArrayList<>();
			try
			{
				classes.add(c);
				Class[] interfaces = c.getInterfaces();
				for (Class i : interfaces)
				{
					classes.add(i);
				}
				for (Class cls : classes)
				{
					for (Class enumClass : cls.getDeclaredClasses())
					{
						if (enumClass.getName().endsWith("$Attributes") || enumClass.getName().endsWith("$Labels"))
						{
							Field[] allFields = enumClass.getFields();
							for (Field f : allFields)
							{
								fields.add(f);
							}
						}
					}

				}
			}
			catch (SecurityException e) {logger.warn("SecurityException: "+e.getMessage());}

			//Write log waring when enum attributes are not saved revision attributes in database
			for (Field f : fields)
			{
				boolean foundField = false;
				for(RA attribute : re.getAttributes())
				{
					if(attribute.getCode().equals(f.getName()))
					{
						foundField= true;
					}
				}
				if(!foundField)
				{
					logger.warn("Revision-Attribute not entered in UI "+re.getCategory().getPosition()+"."+re.getPosition()+": Class ->"+c.getName() +" -> Field: " + f.getName());
				}
			}

			for(RA attribute : re.getAttributes())
			{
				if(attribute.getCode()!=null && attribute.getCode().trim().length()>0)
				{
					labels.get(c.getSimpleName()).put(attribute.getCode(), attribute.getName());
					descriptions.get(c.getSimpleName()).put(attribute.getCode(), attribute.getDescription());
				}
			}
		}
		catch (ClassNotFoundException e) {logger.warn("CNFE: "+re.getCode());}
	}



	private boolean isLoadedRevisionEntity(String jscnRe)
	{
		 boolean x = missingLabelHandler.mapEntities.containsKey(jscnRe)? true : false;
		 return x;
	}

	@Override public List<RE> allEntities() {return missingLabelHandler.allEntities();}

	private Hashtable<String, Map<String, L>> getTempLabelHashtable()
	{
		return new Hashtable<String,Map<String,L>>()
		{
			final static long serialVersionUID = 1L;
			@Override
			public   Map<String, L> get(Object key)
			{
				String missingCode = (String)key;
				Map<String, L> m = super.get(key);
				if (Objects.nonNull(m))
				{
					missingLabelHandler.checkMissingTranslationInMap(m, entityJscn, missingCode);
					return m;
				}
				return getLangMap(entityJscn,missingCode);
			}
		};
	}

	private Map<String, L> getLangMap(String missingEntity, String missingCode)
	{
		return new HashMap<String, L>()
		{
			final static long serialVersionUID = 1L;
			@Override
			public L get(Object key)
			{
				L langLabel = super.get(key);
				if (langLabel != null)
				{
			        return langLabel;
			    }
				else
				{
					missingLabelHandler.updateMissingTranslation(missingEntity,missingCode,(String)key);
					try
					{
						L l = cL.newInstance();
						l.setLkey((String)key);
						return l;
					}
					catch (InstantiationException | IllegalAccessException e)
					{
						return null;
					}
				}
			}
    	};
    }


}