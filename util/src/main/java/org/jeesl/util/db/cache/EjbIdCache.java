package org.jeesl.util.db.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.cache.JeeslIdCache;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIdCache <T extends EjbWithId> implements JeeslIdCache<T>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIdCache.class);

	private final JeeslFacade fUtils;
	private final Class<T> c;
	
	private final Map<Long,T> map; public Map<Long,T> getMap() {return map;}

	public EjbIdCache(Class<T> c)
	{
		this(c,null);
	}
	public EjbIdCache(Class<T> c, JeeslFacade fUtils)
	{
		this.c=c;
		this.fUtils=fUtils;
		map = new HashMap<>();
	}
	
	public T ejb(long id)
	{
		if(!map.containsKey(id) && fUtils!=null)
		{
			try
			{
				map.put(id, fUtils.find(c,id));
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
			
		}
		return map.get(id);
	}
	
	public void populate(List<T> ids)
	{
		for(T t : ids) {map.put(t.getId(),t);}
	}
	
	public boolean contains(long id) {return map.containsKey(id);}
}