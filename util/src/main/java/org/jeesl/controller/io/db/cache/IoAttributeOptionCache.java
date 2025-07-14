package org.jeesl.controller.io.db.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.cache.JeesCodeCache;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoAttributeOptionCache <CRITERIA extends JeeslAttributeCriteria<?,?,?,?,?,OPTION,?>,
										OPTION extends JeeslAttributeOption<?,?,CRITERIA>>
							implements JeesCodeCache<OPTION>
{
	final static Logger logger = LoggerFactory.getLogger(IoAttributeOptionCache.class);
	
	private  JeeslIoAttributeFacade <?,?,CRITERIA,?,OPTION,?,?,?,?> fAttribute;
//	private final IoSsiDataFactoryBuilder<?,?,?,MAPPING,ATTRIBUTE,?,?,?,ENTITY,?,?> fbSsi;
	
	private  Class<T> cT;
	
	private final Map<String,OPTION> map;
	
	public IoAttributeOptionCache()
	{
		map = new HashMap<>();
	}
	
	public IoAttributeOptionCache<CRITERIA,OPTION> init(List<OPTION> options)
	{
		for(OPTION o : options)
		{
			map.put(o.getCode(), o);
		}
		return this;
	}
	
	public OPTION ejb(String remoteCode) throws JeeslNotFoundException
	{
		if(!map.containsKey(remoteCode)) {throw new JeeslNotFoundException("No Entity for: "+remoteCode);}
		return map.get(remoteCode);
	}
}