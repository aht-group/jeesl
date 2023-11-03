package org.jeesl.util.db.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbNonUniquieCodeCache <T extends EjbWithNonUniqueCode>
{
	final static Logger logger = LoggerFactory.getLogger(EjbNonUniquieCodeCache.class);
	
	private final Map<String,T> map; public Map<String, T> getMap() {return map;}
	
	public static <T extends EjbWithNonUniqueCode> EjbNonUniquieCodeCache<T> instance() {return new EjbNonUniquieCodeCache<>();}
	public EjbNonUniquieCodeCache()
	{
		map = new HashMap<>();
	}
	
	public EjbNonUniquieCodeCache<T> addAll(List<T> list)
	{
		map.clear();
		for(T t : list) {map.put(t.getCode(),t);}
		return this;
	}
	
	public int size() {return map.size();}
	public boolean contains(String code) {return map.containsKey(code);}
	
	public <E extends Enum<E>> T ejb(E code)
	{
		if(!map.containsKey(code.toString())) {throw new RuntimeException("Code "+code+" not available");}
		return ejb(code.toString());
	}
	public T ejb(String code)
	{
		return map.get(code);
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
	
	public boolean equals(T ejb, String code)
	{
		if(Objects.isNull(ejb) || Objects.isNull(code)) {return false;}
		return ejb.equals(this.ejb(code));
	}
	public boolean equalsOr(T ejb, String a, String b)
	{
		return this.equals(ejb,a) || this.equals(ejb,b);
	}
}