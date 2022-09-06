package org.jeesl.factory.ejb.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCodeFactory
{
	final static Logger logger = LoggerFactory.getLogger(EjbCodeFactory.class);

	public static <T extends EjbWithCode> List<String> toListCode(Collection<T> list)
    {
    	List<String> result = new ArrayList<String>();
    	for(T s : list)
    	{
    		result.add(s.getCode());
    	}
        return result;
    }
	
    public static <T extends EjbWithCode> Map<String,T> toMapCode(Collection<T> list)
    {
    	Map<String,T> map = new Hashtable<String,T>();
    	for(T s : list)
    	{
    		map.put(s.getCode(), s);
    	}
        return map;
    }
    
    public static <T extends EjbWithNonUniqueCode> Map<String,T> toMapNonUniqueCode(Collection<T> list)
    {
    	Map<String,T> map = new Hashtable<String,T>();
    	for(T s : list)
    	{
    		map.put(s.getCode(), s);
    	}
        return map;
    }
}