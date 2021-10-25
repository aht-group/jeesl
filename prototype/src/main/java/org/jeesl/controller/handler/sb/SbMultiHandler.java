package org.jeesl.controller.handler.sb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.sb.SbToggleBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

public class SbMultiHandler <T extends EjbWithId> implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(SbMultiHandler.class);
	private static final long serialVersionUID = 1L;

	private final Class<T> cT;
	private final SbToggleBean bean; 
	
	private List<T> list; public List<T> getList() {return list;} public void setList(List<T> list) {this.list = list;}
	private final List<T> selected; public List<T> getSelected() {return selected;}
	private Map<T,Boolean> map; public Map<T,Boolean> getMap() {return map;}
	
	public SbMultiHandler(final Class<T> cT, List<T> list){this(cT,list,null);}
	public SbMultiHandler(final Class<T> cT, SbToggleBean bean){this(cT,new ArrayList<T>(),bean);}
	public SbMultiHandler(final Class<T> cT, List<T> list, SbToggleBean bean)
	{
		this.cT=cT;
		this.list=list;
		this.bean=bean;
		map = new ConcurrentHashMap<T,Boolean>();
		selected = new ArrayList<T>();
		refresh();
	}
	
	public void clear()
	{
		list.clear();
		selected.clear();
		map.clear();
	}
	
	public <E extends Enum<E>, S extends EjbWithCode> void add(JeeslFacade fUtils, Class<S> c, E code){this.add(fUtils, c, code.toString());}
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>, S extends EjbWithCode> void add(JeeslFacade fUtils, Class<S> c, String code)
	{
		if(list==null) {list = new ArrayList<T>();}
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
	
	public void toggleNone(){selectNone();callbackToggledToBean();}
	public void selectNone()
	{
		map.clear();
		for(T t : list){map.put(t,false);}
		refresh();
	}
	
	public void toggleAll(){selectAll();callbackToggledToBean();}
	public void selectAll()
	{
		map.clear();
		for(T t : list){map.put(t,true);}
		refresh();
	}
	
	public void fillAndSelect(List<T> items)
	{
		this.list=items;
		preSelect(items);
	}
	public void preSelect(List<T> items)
	{
		for(T t : items)
		{
			if(list.contains(t)){map.put(t,true);}
		}
		refresh();
	}
	
	public void preSelect(T t)
	{
		map.put(t,true);
		refresh();
	}
	
	@SuppressWarnings("unchecked") public <E extends Enum<E>> void preSelect(E... codes) {preRefresh(true, codes);}
	@SuppressWarnings("unchecked") public <E extends Enum<E>> void preDeselect(E... codes) {preRefresh(false, codes);}
	@SuppressWarnings("unchecked") public <E extends Enum<E>> void preRefresh(boolean value, E... codes)
	{
		if(EjbWithCode.class.isAssignableFrom(cT))
		{
			for(T t : list)
			{
				EjbWithCode c = (EjbWithCode)t;
				for(E code : codes)
				{
					if(c.getCode().equals(code.toString()))
					{
						map.put(t,value);
					}
				}
			}
		}
		else {logger.error(cT.getSimpleName()+" is not a "+EjbWithCode.class.getSimpleName());}
		refresh();
	}
	
	public void select(T t)
	{
		map.put(t,true);
		refresh();
	}

	public void toggle(T type)
	{
		if(!map.containsKey(type)){map.put(type,true);}
		else{map.put(type,!map.get(type));}
		refresh();
		callbackToggledToBean();
	}
	
	public void callbackToggledToBean()
	{
		try {if(bean!=null){bean.toggled(cT);}}
		catch (JeeslLockingException e) {e.printStackTrace();}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
	}

	private void refresh()
	{
		selected.clear();
		for(T t : list)
		{
			if(!map.containsKey(t)) {map.put(t,false);}
			if(map.get(t)){selected.add(t);}
		}
	}
	
	public boolean isSelected(T t)
	{
		return map.containsKey(t) && map.get(t);
	}
	
	public boolean getHasMore(){return list.size()>1;}
	public boolean getHasNone(){return list.isEmpty();}
	public boolean getHasSome(){return !list.isEmpty();}
	public boolean getHasSelected(){return hasSelected();}
	public boolean hasSelected(){return !selected.isEmpty();}
	public boolean getHasSelectedMore(){return selected.size()>1;}
	public boolean allSelected(){return selected.size()==list.size();}

	public void debug(boolean debug)
	{
		if(debug)
		{
			logger.info(StringUtil.stars());
			logger.info("List "+list.size());
			logger.info("Selected "+selected.size());
			logger.info("Map "+map.size());
			for(T t : list)
			{
				logger.info("\t"+map.get(t)+" "+t.toString()+" "+selected.contains(t));
			}
		}
	}
}