package org.jeesl.controller.handler.io.label;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.bean.JeeslLabelBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMissingLabelsHandler<L extends JeeslLang,D extends JeeslDescription,
								RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
								RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
								RML extends JeeslRevisionMissingLabel>
	implements Serializable,JeeslLabelBean<RE>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslMissingLabelsHandler.class);
	private static final long serialVersionUID = 1L;

	private JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?,RML> fRevision;
	private Class<RML> cRml;

	public final Map<String,RE> mapEntities; public Map<String,RE> getMapEntities() {return mapEntities;}

	private final List<RML> missingLabelCollection; public List<RML> getMissingLabelCollection() {return missingLabelCollection;}

	public RML tempRevisionMissingLabel;
	public List<String> loadedJscnRevisionEntity;


	public JeeslMissingLabelsHandler(JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?,RML> fRevision,
			final Class<RE> cRE, final Class<L> cL, final Class<RML> cRml)
	{
		this.fRevision=fRevision;
		this.cRml = cRml;
		missingLabelCollection = new ArrayList<RML>();
        mapEntities = new HashMap<String,RE>();
	}

	public void checkMissingTranslationInMap(Map<String,L> m, String missingLabelEntity, String missingLabelCode)
	{
		for (java.util.Map.Entry<String,L> entry : m.entrySet())
		{
			if(entry.getValue().getLang().isEmpty())
			{
				updateMissingTranslation(missingLabelEntity,missingLabelCode,entry.getValue().getLkey());
			}
		}
	}

	public void updateMissingTranslation(String missingEntity, String missingCode, String localString)
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
			try
			{
				if(Objects.nonNull(rml) && Objects.equals(rml.getMissingEntity(), missingEntity) && Objects.equals(rml.getMissingCode(), missingCode) && Objects.equals(rml.getMissingLocal(),localString))
				{
					return true;
				}
			}
			catch(NullPointerException e) {logger.info("Null revision missing label found : " + e.getMessage());}
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

	public void updateMissingRevisionEntity(String entityJscn)
	{
		mapEntities.put(entityJscn, null);
		updateMissingTranslation(entityJscn,"","");
	}
	
	/*
	 Alternative imp
	{
		//Check if Revision Entity are really Missing in Revision
		try
		{
			RE re =  fRevision.fRevisionEntity(entityJscn);
		}
		catch (JeeslNotFoundException e)
		{
			//If Not found put it in Missing revision table
			mapEntities.put(entityJscn, null);
			updateMissingTranslation(entityJscn,"","");
			e.printStackTrace();
		}
	}
	*/

	public void load(RE re)
	{
		try
		{
			Class<?> c = Class.forName(re.getCode());
			mapEntities.put(c.getSimpleName(),re);
		}
		catch (ClassNotFoundException e) {e.printStackTrace();}
	}

	@Override
	public void reload(RE re)
	{
		try
		{
			Class<?> c = Class.forName(re.getCode());
			if(mapEntities.containsKey(c.getSimpleName())) {mapEntities.remove(c.getSimpleName());}
		}
		catch (ClassNotFoundException e) {e.printStackTrace();}
	}


	@Override
	public List<RE> allEntities()
	{
		return new ArrayList<RE>(mapEntities.values());
	}


}