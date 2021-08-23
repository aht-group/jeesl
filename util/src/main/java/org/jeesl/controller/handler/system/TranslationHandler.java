package org.jeesl.controller.handler.system;

import java.io.File;
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
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.ObjectIO;

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
	private Class<RML> cRml;

	private final Map<String,Map<String,L>> entities; public Map<String,Map<String,L>> getEntities() {return entities;}
	private final Map<String,Map<String,Map<String,L>>> labels; public Map<String, Map<String, Map<String,L>>> getLabels() {return labels;}
	private final Map<String,Map<String,Map<String,D>>> descriptions;public Map<String, Map<String, Map<String,D>>> getDescriptions() {return descriptions;}

	public final Map<String,RE> mapEntities; public Map<String,RE> getMapEntities() {return mapEntities;}

	private final List<RML> missingLabelCollection;
	public String tempMissingLabelEntity;
	public RML tempRevisionMissingLabel;


	public TranslationHandler(JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?,RML> fRevision, final Class<RE> cRE, final Class<L> cL, final Class<RML> cRml)
	{
		this.fRevision=fRevision;
		this.cRE = cRE;
		this.cL = cL;
		this.cRml = cRml;
		missingLabelCollection = new ArrayList<RML>();
		tempMissingLabelEntity ="";

        entities = new HashMap<String,Map<String,L>>()
        {
			private static final long serialVersionUID = 1L;

			@Override public Map<String, L> get(Object key)
			{
				Map<String,L> m = super.get(key);
				tempMissingLabelEntity = (String)key;
				if (m != null)
				{
					for (java.util.Map.Entry<String,L> entry : m.entrySet())
					{
						if(entry.getValue().getLang().isEmpty())
						{
							collectMissingLabel(tempMissingLabelEntity,"",entry.getValue().getLkey());
						}
					}
					return m;
				}
			    else {return getLangMap(tempMissingLabelEntity,null);}
			}
        };

        labels = new HashMap<String,Map<String,Map<String,L>>>()
        {
        	private static final long serialVersionUID = 1L;

			@Override
			public  Map<String, Map<String, L>> get(Object key)
			{
				Map<String, Map<String, L>> m = super.get(key);
				tempMissingLabelEntity =(String)key;
				if (m != null)
				{
			        return m;
			    }
				return getTempLabelHashtable();
			}
        };

        descriptions = new HashMap<String,Map<String,Map<String,D>>>();
        mapEntities = new HashMap<String,RE>();
	}
	
	public void reloadFromDb()
	{
		loadAll();
	}
	public void reloadFromDb(File fTmpCache)
	{
		loadAll();
		
		ObjectIO.save(new File(fTmpCache,"thEntities.ser"),entities);
		ObjectIO.save(new File(fTmpCache,"thLabels.ser"),labels);
		ObjectIO.save(new File(fTmpCache,"thDescriptions.ser"),descriptions);
	}
	
	@SuppressWarnings("unchecked")
	public void reloadFromFile(File fTmpCache)
	{
		entities.putAll((Map<String,Map<String,L>>)ObjectIO.load(new File(fTmpCache,"thEntities.ser")));
		labels.putAll((Map<String,Map<String,Map<String,L>>>)ObjectIO.load(new File(fTmpCache,"thLabels.ser")));
		descriptions.putAll((Map<String,Map<String,Map<String,D>>>)ObjectIO.load(new File(fTmpCache,"thDescriptions.ser")));
	}

	private void loadAll()
	{
		List<RE> list = fRevision.all(cRE);
        logger.info("building "+list.size());

		for(RE re : list)
		{
			load(re);
		}
	}

	@Override public void reload(RE re)
	{
		try
		{
			Class<?> c = Class.forName(re.getCode());
			if(mapEntities.containsKey(c.getSimpleName())) {mapEntities.remove(c.getSimpleName());}
			if(entities.containsKey(c.getSimpleName())) {entities.remove(c.getSimpleName());}
			if(labels.containsKey(c.getSimpleName())) {labels.remove(c.getSimpleName());}
			if(descriptions.containsKey(c.getSimpleName())) {descriptions.remove(c.getSimpleName());}
			load(re);
		}
		catch (ClassNotFoundException e) {logger.warn("CNFE: "+re.getCode());}
	}

	public void load(RE re)
	{
		try
		{
			Class<?> c = Class.forName(re.getCode());

			re = fRevision.load(cRE, re);
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
			//mapEntities.put(c.getSimpleName(),re);
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

	@Override public List<RE> allEntities() {return new ArrayList<RE>(mapEntities.values());}

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
				if (m != null)
				{
					for (java.util.Map.Entry<String, L> entry : m.entrySet())
					{
						if(entry.getValue().getLang().isEmpty())
						{
							collectMissingLabel(tempMissingLabelEntity,missingCode,entry.getValue().getLkey());
						}
					}
					return m;
				}
			    else
			    {
			    	return getLangMap(tempMissingLabelEntity,missingCode);
			    }
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
					collectMissingLabel(missingEntity,missingCode,(String)key);
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

	private void collectMissingLabel(String missingEntity, String missingCode, String localString)
	{
		try
		{
			if(!containsInMissingLabelCollection(missingEntity,missingCode,localString))
			{
				RML rml;
				rml = cRml.newInstance();
				rml.setMissingEntity(missingEntity);
				rml.setMissingCode(missingCode);
				rml.setMissingLocal(localString);
				missingLabelCollection.add( rml);
			}
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			logger.info("Can not add Missing Entity: " + missingEntity + " Code : "+ missingCode  + "Lang: " + localString );
		}
	}

	private boolean containsInMissingLabelCollection(String missingEntity, String missingCode, String localString)
	{
		for(RML rml : missingLabelCollection)
		{
			if(Objects.equals(rml.getMissingEntity(), missingEntity) && Objects.equals(rml.getMissingCode(), missingCode) && Objects.equals(rml.getMissingLocal(),localString))
			{
				return true;
			}
		}
		return false;
	}

	public void updateMissingLabels()
	{
		for(RML rml : missingLabelCollection)
		{
			fRevision.addMissingLabel(rml);
		}
	}
}