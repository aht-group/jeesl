package org.jeesl.util.db.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCodeCache <T extends EjbWithCode>
{
	final static Logger logger = LoggerFactory.getLogger(EjbCodeCache.class);

	private JeeslFacade fUtils;
	private final Class<T> c;
	
	private final Map<String,T> map;
	
	public EjbCodeCache(Class<T> c, JeeslFacade fUtils)
	{
		this.c=c;
		this.fUtils=fUtils;
		map = new HashMap<>();
	}
	
	public EjbCodeCache(Class<T> c, List<T> list)
	{
		this.c=c;
		map = new HashMap<>();
		for(T t : list) {map.put(t.getCode(),t);}
	}
	
	public int size() {return map.size();}
	public boolean contains(String code) {return map.containsKey(code);}
	
	public <E extends Enum<E>> T ejb(E code) {return ejb(code.toString());}
	public T ejb(String code)
	{
		return ejb(code,true);
	}
	public T ejb(String code, boolean withStackTrace)
	{
		if(!map.containsKey(code))
		{
			try
			{
				map.put(code, fUtils.fByCode(c,code));
			}
			catch (JeeslNotFoundException e)
			{
				if(withStackTrace) {e.printStackTrace();}
			}
		}
		return map.get(code);
	}
	
	public void verify(String code) throws JeeslNotFoundException
	{
		if(!map.containsKey(code))
		{
			map.put(code, fUtils.fByCode(c,code));
		}
	}
	
	public <E extends Enum<E>> boolean equals(T ejb, E code)
	{
		return ejb.equals(this.ejb(code));
	}
	
	public <E extends Enum<E>> boolean equalsOr(T ejb, E a, E b)
	{
		return this.equals(ejb,a) || this.equals(ejb,b);
	}
}