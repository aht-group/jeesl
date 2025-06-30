package org.jeesl.util.db.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.ejb.JeeslNotFoundRuntimeException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCodeCache <T extends EjbWithCode>
{
	final static Logger logger = LoggerFactory.getLogger(EjbCodeCache.class);

	private boolean withNotFoundStackTrace; public EjbCodeCache<T> withNotFoundStackTrace(boolean value) {this.withNotFoundStackTrace=value; return this;}
	private boolean withNotFoundRuntimeException; public EjbCodeCache<T> withNotFoundRuntimeException(boolean value) {this.withNotFoundRuntimeException=value; return this;}
	
	private JeeslFacade facade; public EjbCodeCache<T> facade(JeeslFacade facade) {this.facade=facade; return this;}
	private final Class<T> c;
	
	private final Map<String,T> map; public Map<String,T> getMap() {return map;}
	
	public static <T extends EjbWithCode> EjbCodeCache<T> instance(Class<T> c) {return new EjbCodeCache<>(c);}
	public EjbCodeCache(Class<T> c)
	{
		this.c=c;
		map = new HashMap<>();
		
		withNotFoundStackTrace = false;
	}
	
	public EjbCodeCache<T> load() {map.clear(); if(Objects.nonNull(facade)) {this.init(facade.all(c));} return this;}
	public EjbCodeCache<T> init(List<T> list) {map.clear(); if(Objects.nonNull(list)) { for(T t : list) {map.put(t.getCode(),t);}} return this;}
	
	public int size() {return map.size();}
	public boolean contains(String code) {return map.containsKey(code);}
	
	public <E extends Enum<E>> T ejb(int value) {return ejb(""+value);}
	public <E extends Enum<E>> T ejb(E code) {return ejb(code.toString());}
	public T ejb(String code)
	{
		if(!map.containsKey(code))
		{
			if(Objects.nonNull(facade))
			{
				try {map.put(code, facade.fByCode(c,code));}
				catch (JeeslNotFoundException e)
				{
					if(withNotFoundStackTrace) {e.printStackTrace();}
					if(withNotFoundRuntimeException) {throw JeeslNotFoundRuntimeException.instance(code).searchClass(c);}
				}
			}
		}
		return map.get(code);
	}
	
	public void verify(String code) throws JeeslNotFoundException
	{
		if(!map.containsKey(code))
		{
			map.put(code, facade.fByCode(c,code));
		}
	}
	
	public <E extends Enum<E>> boolean notEquals(T ejb, E code) {return !this.equals(ejb,code);}
	public <E extends Enum<E>> boolean equals(T ejb, E code)
	{
		if(Objects.isNull(ejb) || Objects.isNull(code)) {return false;}
		return ejb.equals(this.ejb(code));
	}
	public <E extends Enum<E>> boolean equalsOr(T ejb, E a, E b)
	{
		return this.equals(ejb,a) || this.equals(ejb,b);
	}
	public <E extends Enum<E>> boolean equalsOr(T ejb, E a, E b, E c)
	{
		return this.equals(ejb,a) || this.equals(ejb,b) || this.equals(ejb,c);
	}
	
	public boolean equals(T ejb, String code)
	{
		if(Objects.isNull(ejb) || Objects.isNull(code)) {return false;}
		return ejb.equals(this.ejb(code));
	}
	public boolean equalsOr(T ejb, String a, String b)
	{
		return this.equals(ejb,a) || this.equals(ejb,b);
	}
	public boolean equalsOr(T ejb, String a, String b, String c)
	{
		return this.equals(ejb,a) || this.equals(ejb,b) || this.equals(ejb,c);
	}
}