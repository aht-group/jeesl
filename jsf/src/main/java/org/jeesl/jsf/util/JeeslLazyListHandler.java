package org.jeesl.jsf.util;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslLazyListHandler <T extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslLazyListHandler.class);
	
	public enum Mode{list,db}
	
	private final List<T> tmp; public List<T> getTmp() {return tmp;}

	public JeeslLazyListHandler()
	{
		tmp = new ArrayList<>();
	}
	
	public void clear() {tmp.clear();}
	public void add(T t) {tmp.add(t);}
	public void addAll(List<T> list) {tmp.addAll(list);}
	public int size() {return tmp.size();}
	
	public Object getRowKey(T t) {return t.getId();}
	public T getRowData(List<T> list, String rowKey)
	{
		long id = new Long(rowKey).longValue();
		for(T t : list)
		{
			if(t.getId()==id)
			{
				return t;
			}
		}
		return null;
	}
	public T getRowData(String rowKey)
	{
		long id = new Long(rowKey).longValue();
		for(T t : tmp)
		{
			if(t.getId()==id)
			{
				return t;
			}
		}
		return null;
	}
	
	public void paginator(List<T> tmp, int first, int pageSize, List<T> result)
	{
		result.clear();
		if(tmp.size() > pageSize)
		{
			try
			{
				if(first+pageSize < tmp.size()){result.addAll(tmp.subList(first, first + pageSize));}
				else {result.addAll(tmp.subList(first,tmp.size()));}
			}
			catch(IndexOutOfBoundsException e){result.addAll(tmp.subList(first, first + (tmp.size() % pageSize)));}
		}
		else {result.addAll(tmp);}
	}
	
	public List<T> paginator(int first, int pageSize)
	{
		List<T> result = new ArrayList<>();
		if(tmp.size() > pageSize)
		{
			try
			{
				if (first+pageSize < tmp.size()){result.addAll(tmp.subList(first, first + pageSize));}
				else{result.addAll(tmp.subList(first,tmp.size()));}
			}
			catch(IndexOutOfBoundsException e){result.addAll(tmp.subList(first, first + (tmp.size() % pageSize)));}
		}
		else {result.addAll(tmp);}
		return result;
	}
}