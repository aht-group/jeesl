package org.jeesl.factory.ejb.util;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbVisibleFactory
{
	final static Logger logger = LoggerFactory.getLogger(EjbVisibleFactory.class);
    
	public static <T extends EjbWithVisible> List<T> toVisible(List<T> list)
	{
		List<T> result = new ArrayList<>();
		for(T t : list) {if(t.isVisible()) {result.add(t);}}
		return result;
	}
}