package org.jeesl.factory.ejb.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.api.exception.JeeslNotUniqueException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.JeeslIdEjb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIdFactory
{
	final static Logger logger = LoggerFactory.getLogger(EjbIdFactory.class);
    
	public static long invert(long id)
	{       
        return -1*id;
	}
	
	public static long positive(long id)
	{   
		if(id>0){return id;}
		else{return -1*id;}
	}

	public static long negative(long id)
	{
		if(id<0){return id;}
		else{return -1*id;}
	}
	
	public static boolean isUnSaved(EjbWithId ejb){return !isSaved(ejb);}
	public static boolean isSaved(EjbWithId ejb){return (ejb!=null && ejb.getId()>0);}
	
	public static boolean isSaved(EjbWithId... ejbs)
	{
		for(EjbWithId ejb : ejbs)
		{
			if(ejb==null || ejb.getId()<=0){return false;}
		}
		return true;
	}
	
	public static <T extends EjbWithId> List<Long> toIds(Collection<T> list){return toIds(list,null);}
	public static <T extends EjbWithId> List<Long> toIds(Collection<T> list, Boolean onlySaved)
	{
		List<Long> results = new ArrayList<Long>();
		for(T ejb : list)
		{
			if(onlySaved==null){results.add(ejb.getId());}
			else
			{
				if(!onlySaved){results.add(ejb.getId());}
				else if(onlySaved && EjbIdFactory.isSaved(ejb)){results.add(ejb.getId());}
			}
		}
		return results;
	}
	public static <T extends EjbWithId> List<Long> toIds(T ejb)
	{
		List<Long> ids = new ArrayList<Long>();
		if(EjbIdFactory.isSaved(ejb)) {ids.add(ejb.getId());}
		return ids;
	}
	
	public static <T extends EjbWithId> List<Long> toLongs(Collection<T> list)
	{
		List<Long> results = new ArrayList<Long>();
		for(T ejb : list)
		{
			results.add(ejb.getId());
		}
		return results;
	}
	
	public static List<Long> toLong(String[] list)
	{
		List<Long> results = new ArrayList<Long>();
		if(list!=null)
		{
			for(String s : list)
			{
				results.add(Long.valueOf(s));
			}
		}
		return results;
	}
	public static List<Long> toLong(List<String> list)
	{
		List<Long> results = new ArrayList<Long>();
		for(String s : list)
		{
			results.add(Long.valueOf(s));
		}
		return results;
	}
	
	public static <T extends EjbWithId> Map<Long,T> toIdMap(List<T> list)
	{
		Map<Long,T> map = new HashMap<Long,T>();
		for(T t : list){map.put(t.getId(), t);}
		return map;
	}
	
	public static <T extends EjbWithId> List<T> toSaved(List<T> list)
	{
		List<T> result = new ArrayList<>();
		for(T t : list) {if(EjbIdFactory.isSaved(t)) {result.add(t);}}
		return result;
	}
	
	public static <T extends EjbWithId> void setNextNegativeId(T ejb, List<T> list)
	{
		Set<Long> existing = new HashSet<Long>(EjbIdFactory.toIds(list));
		
		boolean search = true;
		long next = -1-existing.size();
		while(search)
		{
			if(existing.contains(next)){next--;}
			else{search=false;}
		}
		ejb.setId(next);
	}
	public static <T extends EjbWithId> void setNextNegativeId(T ejb, Map<Long,T> map)
	{
		Set<Long> existing = new HashSet<Long>(EjbIdFactory.toIds(map.values()));
		
		boolean search = true;
		long next = 0-existing.size();
		while(search)
		{
			if(next==0){next--;}
			else if(existing.contains(next)){next--;}
			else{search=false;}
		}
		ejb.setId(next);
	}
	public static <T extends EjbWithId> void setNegativeIds(List<T> list)
	{
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setId(-1-i);
		}
	}
	
	public static <T extends EjbWithId> void negativeToZero(List<T> list)
	{
		for(T ejb : list) {negativeToZero(ejb);}
	}
	
	public static <T extends EjbWithId> void negativeToZero(T ejb)
	{
		if(ejb.getId()<0){ejb.setId(0);}
	}
	
	public static <T extends EjbWithId> String toIdList(List<T> list)
	{
		List<Long> result = new ArrayList<>();
		for(T l : list){result.add(l.getId());}
		return StringUtils.join(result,",");
	}
	public static <T extends EjbWithId> List<EjbWithId> toEjbIdList(List<T> list)
	{
		List<EjbWithId> result = new ArrayList<EjbWithId>();
		for(T l : list){result.add(l);}
		return result;
	}
	
	public static JeeslIdEjb toIdEjb(Long id)
	{
		if(Objects.isNull(id)) {return null;}
		else
		{
			JeeslIdEjb ejb = new JeeslIdEjb();
			ejb.setId(id);
			return ejb;
		}
	}
	
	public static <T extends EjbWithId> void updateList(List<T> list, T ejb)
	{
		int index = list.indexOf(ejb);
		if(index>=0)
		{
			list.set(index,ejb);
			logger.info("List updated on position "+index+" with "+ejb.toString());
		}
		else
		{
			logger.warn("List not updated (index="+index+")with "+ejb.toString());
		}
	}
	
	public static <T extends EjbWithId> boolean equals(T a, T b)
	{
		if(a==null && b==null) {return true;}
		else if(a!=null && b==null) {return false;}
		else if(a==null && b!=null) {return false;}
		else {return a.equals(b);}
	}
	
	public static <T extends EjbWithId> void replace(List<T> list, T ejb)
	{
		int index = list.indexOf(ejb);
		if(index>=0) {list.set(index,ejb);}
	}
	
	public static <T extends EjbWithId> void integrate(List<T> list, T ejb)
	{	// integrate is a "replace or add" operation
//		if(!Collections.replaceAll(mapCompany.get(rref),company,company)){mapCompany.get(rref).add(company);}
		int index = list.indexOf(ejb);
		if(index>=0) {list.set(index,ejb);}
		else {list.add(ejb);}
	}
	
	public static <T extends EjbWithId> void remove(List<T> list, T ejb)
	{
		int index = list.indexOf(ejb);
		if(index>=0) {list.remove(index);}
	}
	
	public static <T extends EjbWithId> T next(List<T> list, T ejb)
	{
		int index = list.indexOf(ejb);
		if(list.size()>index)
		{
			return list.get(index+1);
		}
		return ejb;
	}
	
	public static <T extends EjbWithId> T fSingle(List<T> list) throws JeeslNotFoundException, JeeslNotUniqueException
	{
		if(list.size()==1) {return list.get(0);}
		if(list.isEmpty()) {throw new JeeslNotFoundException("Not found");}
		else {throw new JeeslNotUniqueException("Multiple");}
	}
	public static <T extends EjbWithId> T fUnique(List<T> list) throws JeeslNotFoundException
	{
		if(list.size()==1) {return list.get(0);}
		if(list.isEmpty()) {throw new JeeslNotFoundException("Not found");}
		else {throw new RuntimeException("Multiple");}
	}
}