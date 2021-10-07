package org.jeesl.controller.handler.sb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.sb.SbSingleBean;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree1Cache;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

public class SbSingleHandler <T extends EjbWithId> implements Serializable,SbSingleBean
{
	final static Logger logger = LoggerFactory.getLogger(SbSingleHandler.class);
	private static final long serialVersionUID = 1L;

	private boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	private final SbSingleBean bean;
	protected final Class<T> c;

	private final List<T> list; public List<T> getList() {return list;} public void setList(List<T> list) {this.list.clear();this.list.addAll(list);}

	private T selection; public T getSelection() {return selection;} public void setSelection(T selected) {this.selection = selected;}
	private T previous;

	public SbSingleHandler(Class<T> c, SbSingleBean bean){this(c,new ArrayList<T>(),bean);}
	public SbSingleHandler(Class<T> c, List<T> list, SbSingleBean bean)
	{
		this.c=c;
		this.bean=bean;
		debugOnInfo = false;
		this.list = new ArrayList<T>();

		if(list==null) {list=new ArrayList<T>();}
		update(list);
		try
		{
			previous = c.newInstance();
			previous.setId(-1);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
	}
	
	public void cache(JeeslTree1Cache<T> cache1)
	{
		this.update(cache1.getCachedL1());
	}

	public void silentCallback()
	{
		try
		{
			if (list.isEmpty())
			{
				//selectSbSingle(null);
			}
			else
			{
				if(list.contains(selection)) {selectSbSingle(selection);}
				else{selectSbSingle(list.get(0));}
			}
		}
		catch (JeeslLockingException e) {}
		catch (JeeslConstraintViolationException e) {}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(item==null)
		{
			previous.setId(-1);
			selection = null;
			if(bean!=null){bean.selectSbSingle(null);}
		}
		else if(c.isAssignableFrom(item.getClass()))
		{
			if(selection!=null) {previous.setId(selection.getId());}
			selection=(T)item;
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName());
			sb.append(" selected with:").append(selection.toString());
			sb.append(" Previous:").append(previous.getId());
			if(debugOnInfo) {logger.info(sb.toString());}
			if(bean!=null){bean.selectSbSingle(item);}
		}
	}

	public <E extends Enum<E>, C extends EjbWithCode> void update(List<C> list, E code) {this.update(list,code.toString());}
	@SuppressWarnings("unchecked")
	public <C extends EjbWithCode> void update(List<C> list, String code)
	{
		T preferred = null;
		List<T> list2 = new ArrayList<T>();
		for(C c : list)
		{
			list2.add((T)c);
			if(c.getCode().equals(code)) {preferred=(T)c;}
		}
		update(list2,preferred);
	}
	
	public void update(List<T> list){update(list, null);}
	public void update(T t, T preferred)
	{
		List<T> list = new ArrayList<T>();
		list.add(t);
		update(list,preferred);
	}
	public void update(List<T> list, T preferred)
	{
		this.list.clear();
		this.list.addAll(list);

		if(selection!=null)
		{
			if(!this.list.contains(selection)){selection=null;}
		}
		
		if(selection==null && preferred!=null)
		{
			if(this.list.contains(preferred)) {selection=preferred;}
		}

		if(selection==null && !this.list.isEmpty())
		{
			selection=this.list.get(0);
		}
	}

	public <E extends Enum<E>, S extends EjbWithCode> void add(JeeslFacade fUtils, Class<S> c, E code){this.add(fUtils,c,code.toString());}
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>, S extends EjbWithCode> void add(JeeslFacade fUtils, Class<S> c, String code)
	{
		try
		{
			S status = fUtils.fByCode(c,code);
			list.add((T)status);
		}
		catch (JeeslNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void clear()
	{
		list.clear();
		logger.info("calling clear list done");
		logger.info("" + list.size());
		selection = null;
		logger.info("calling clear selection done");
	}

	public boolean getHasNone(){return list.isEmpty();}
	public boolean getHasSome(){return !list.isEmpty();}
	public boolean getHasMore(){return list.size()>1;}
	public boolean isSelected(){return selection!=null;}
	public boolean getTwiceSelected() {return previous.equals(selection);}

	public void setDefault(T t)
	{
		if(list.contains(t)) {selection = t;}
		else {setDefault();}
	}

	public void setDefault()
	{
		selection=null;
		if(list!=null && !list.isEmpty()){selection = list.get(0);}
	}
	
	public void selectNext() throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(this.isSelected())
		{
			int index = list.indexOf(selection);
			int next = (index+1) % list.size();
			this.selectSbSingle(list.get(next));
		}
	}
	public void selectPrevious() throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(this.isSelected())
		{
			int index = list.indexOf(selection);
			int next = (index-1);
			if(next<0) {next=list.size();}
			this.selectSbSingle(list.get(next));
		}
	}
	
	public void debug()
	{
		logger.info(StringUtil.stars());
		for(T t : list)
		{
			logger.info(t.toString()+" "+(t.equals(selection)));
		}
	}
}