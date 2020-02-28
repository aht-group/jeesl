package org.jeesl.controller.processor.finance;

import java.util.Map;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapSummer
{
	final static Logger logger = LoggerFactory.getLogger(MapSummer.class);
	
	public static <T extends EjbWithId> void add(Map<T,Double> map, T key, double value)
	{
		if(!map.containsKey(key)){map.put(key, 0d);}
		map.put(key, value+map.get(key));
	}
}