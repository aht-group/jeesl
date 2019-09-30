package org.jeesl.controller.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslLabelBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;

public class TranslationHandler<L extends UtilsLang,D extends UtilsDescription,
								RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
								RA extends JeeslRevisionAttribute<L,D,RE,?,?>>
	implements Serializable,JeeslLabelBean<RE>
{
	final static Logger logger = LoggerFactory.getLogger(TranslationHandler.class);
	private static final long serialVersionUID = 1L;
	
	private final Class<RE> cRE;
	
	private final JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?> fRevision;
	
	private final Map<String,Map<String,L>> entities; public Map<String,Map<String,L>> getEntities() {return entities;}
	private final Map<String,Map<String,Map<String,L>>> labels; public Map<String, Map<String, Map<String,L>>> getLabels() {return labels;}
	private final Map<String,Map<String,Map<String,D>>> descriptions;public Map<String, Map<String, Map<String,D>>> getDescriptions() {return descriptions;}

	public final Map<String,RE> mapEntities; public Map<String,RE> getMapEntities() {return mapEntities;}
	
	public TranslationHandler(JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?> fRevision, final Class<RE> cRE)
	{
		this.cRE = cRE;
		this.fRevision=fRevision;
		
        entities = new HashMap<String,Map<String,L>>();
        labels = new HashMap<String,Map<String,Map<String,L>>>();
        descriptions = new HashMap<String,Map<String,Map<String,D>>>();
        
        mapEntities = new HashMap<String,RE>();
        
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
			mapEntities.put(c.getSimpleName(),re);
			
			re = fRevision.load(cRE, re);
			if(entities.containsKey(c.getSimpleName())){logger.warn("Duplicate classs in Revisions "+re.getCode());}
			
			entities.put(c.getSimpleName(), re.getName());
			labels.put(c.getSimpleName(), new Hashtable<String,Map<String,L>>());
			descriptions.put(c.getSimpleName(), new Hashtable<String,Map<String,D>>());
			
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
}