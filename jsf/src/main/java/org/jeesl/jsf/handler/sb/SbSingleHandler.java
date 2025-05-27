package org.jeesl.jsf.handler.sb;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.StringUtil;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.controller.handler.tree.cache.JeeslTree1Cache;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbSingleHandler <T extends EjbWithId> implements SbSingleBean
{
	final static Logger logger = LoggerFactory.getLogger(SbSingleHandler.class);
	private static final long serialVersionUID = 1L;

	private boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	private final SbSingleBean bean;
	protected final Class<T> c;

	private final List<T> list; public List<T> getList() {return list;} public void setList(List<T> list) {this.list.clear(); this.list.addAll(list);}

	private T selection; public T getSelection() {return selection;} public void setSelection(T selected) {this.selection = selected;}
	private T previous;

	public SbSingleHandler(SbSingleBean bean) {this(null,new ArrayList<T>(),bean);}
	public SbSingleHandler(Class<T> c, SbSingleBean bean) {this(c,new ArrayList<T>(),bean);}
	public SbSingleHandler(Class<T> c, List<T> list, SbSingleBean bean)
	{
		this.c=c;
		this.bean=bean;
		debugOnInfo = false;
		this.list = new ArrayList<T>();

		if(list==null) {list=new ArrayList<T>();}
		this.update(list);
		
		if(Objects.nonNull(c))
		{
			try
			{
				previous = c.getDeclaredConstructor().newInstance();
				previous.setId(-1);
			}
			catch (InstantiationException e) {e.printStackTrace();}
			catch (IllegalAccessException e) {e.printStackTrace();}
			catch (IllegalArgumentException e) {e.printStackTrace();}
			catch (InvocationTargetException e) {e.printStackTrace();}
			catch (NoSuchMethodException e) {e.printStackTrace();}
			catch (SecurityException e) {e.printStackTrace();}
		}
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
				else {selectSbSingle(list.get(0));}
			}
		}
		catch (JeeslLockingException e) {}
		catch (JeeslConstraintViolationException e) {}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(Objects.isNull(item))
		{
			previous.setId(-1);
			selection = null;
			if(Objects.nonNull(bean)) {bean.selectSbSingle(null);}
		}
		else
		{
			if(selection!=null) {previous.setId(selection.getId());}
			selection=(T)item;
			if(debugOnInfo)
			{
				StringBuilder sb = new StringBuilder();
				sb.append(this.getClass().getSimpleName());
				sb.append(" selected with:").append(selection.toString());
				sb.append(" Previous:").append(previous.getId());
				logger.info(sb.toString());
			}
			if(Objects.nonNull(bean)) {bean.selectSbSingle(item);}
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
	
	public void addAll(Collection<T> collection)
	{
		if(collection!=null)
		{
			for(T t : collection)
			{
				this.add(t);
			}
		}
	}
	public void add(T item)
	{
		if(item!=null && !list.contains(item)) {list.add(item);}
	}

	public void clear()
	{
		list.clear();
		selection = null;
	}

	public boolean getHasNone(){return list.isEmpty();}
	public boolean getHasSome(){return !list.isEmpty();}
	public boolean getHasMore(){return list.size()>1;}
	
	public boolean getTwiceSelected() {return previous.equals(selection);}
	
	public boolean isSelected() {return selection!=null;}
	public <E extends Enum<E>> boolean isSelected(E code)
	{
		if(!isSelected()) {return false;}
		if(Objects.nonNull(c))
		{
			logger.error("C not defined");
			return false;
		}
		
		if(c.isAssignableFrom(EjbWithCode.class))
		{
			EjbWithCode w = (EjbWithCode)selection;
			return w.getCode().equals(code.toString());
		}
		else
		{
			logger.error("The class "+c.getSimpleName()+" does not implement "+EjbWithCode.class.getSimpleName());
			return false;
		}
	}

	public void setDefault()
	{
		selection=null;
		if(list!=null && !list.isEmpty()){selection = list.get(0);}
	}
	
	public void setLast()
	{
		selection=null;
		if(ObjectUtils.isNotEmpty(list)) {selection = list.get(list.size()-1);}
	}
	
	public void setDefault(T t)
	{
		this.setDefault(t,null);
	}
	public void setDefault(T t1, T t2)
	{
		if(Objects.nonNull(t1) && list.contains(t1)) {selection = t1;}
		else if(Objects.nonNull(t2) && list.contains(t2)) {selection = t2;}
		else {setDefault();}
	}
	
	public <E extends Enum<E>> void setDefault(E code) {this.setDefault(code.toString());}
	public <E extends Enum<E>> void setDefault(String code)
	{
		if(Objects.nonNull(c) && EjbWithCode.class.isAssignableFrom(c) || EjbWithNonUniqueCode.class.isAssignableFrom(c))
		{
			for(T t : list)
			{
				if(((EjbWithCode)t).getCode().equals(code))
				{
					setDefault(t);
					break;
				}
			}
		}
	}
	
	public void selectNext() throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(this.isSelected())
		{
			int index = list.indexOf(selection);
			int next = (index+1) % list.size();
			if(debugOnInfo) {logger.info("selectNext: index="+index+" next="+next);}
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
			if(debugOnInfo) {logger.info("selectPrevious: index="+index+" next="+next);}
			this.selectSbSingle(list.get(next));
		}
	}
	
	public void debug(boolean debug)
	{
		if(debug)
		{
			logger.info(StringUtil.stars());
			for(T t : list)
			{
				StringBuilder sb = new StringBuilder();
				sb.append(list.indexOf(t)).append(" ");
				sb.append(Objects.nonNull(t) ? t.toString() : "--");
				sb.append(" ");
				sb.append(t.equals(selection));
				logger.info(sb.toString());
			}
		}
	}
}