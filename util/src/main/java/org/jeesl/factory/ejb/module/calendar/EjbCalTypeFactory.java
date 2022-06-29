package org.jeesl.factory.ejb.module.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCalTypeFactory<CT extends JeeslStatus<?,?,CT>,
									IT extends JeeslStatus<?,?,IT>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbCalTypeFactory.class);
	
	final Class<CT> cCalendarType;
	final Class<IT> cItemType;
	
	public EjbCalTypeFactory(final Class<CT> cCalendarType, final Class<IT> cItemType)
	{  
        this.cCalendarType = cCalendarType;
        this.cItemType = cItemType;
	}
	
	public Map<CT,List<IT>> buildMap(JeeslFacade facade)
	{
		Map<CT,List<IT>> map = new HashMap<>();
		
		for(IT iType : facade.all(cItemType))
		{
			CT cType = iType.getParent();
			logger.info(cType.toString()+" "+iType.toString());
			if(!map.containsKey(cType)) {map.put(cType, new ArrayList<>());}
			map.get(cType).add(iType);
		}
		
		return map;
	}
}