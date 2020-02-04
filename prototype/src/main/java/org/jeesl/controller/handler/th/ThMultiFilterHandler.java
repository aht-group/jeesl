package org.jeesl.controller.handler.th;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jeesl.interfaces.bean.th.ThMultiFilterBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.th.ThMultiFilter;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.exlp.util.io.StringUtil;

public class ThMultiFilterHandler <T extends EjbWithId> implements Serializable,ThMultiFilter
{
	final static Logger logger = LoggerFactory.getLogger(ThMultiFilterHandler.class);
	private static final long serialVersionUID = 1L;

	private final ThMultiFilterBean bean; 
	
	private final List<T> list; public List<T> getList() {return list;} public void setList(List<T> list) {this.list.clear();this.list.addAll(list);}
	private final List<T> selected; public List<T> getSelected() {return selected;}
	private Map<T,Boolean> map; public Map<T,Boolean> getMap() {return map;}
	
	public ThMultiFilterHandler(ThMultiFilterBean bean)
	{
		this.bean=bean;
		list = new ArrayList<>();
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
	
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>, S extends JeeslStatus<S,L,D>, L extends JeeslLang, D extends JeeslDescription> void add(JeeslFacade fUtils, Class<S> c, E code)
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
	
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> void preSelect(Class<T> cT, E... codes)
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
						map.put(t,true);
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
		logger.info("Toggle "+type);
		if(!map.containsKey(type)){map.put(type,true);}
		else{map.put(type,!map.get(type));}
		refresh();
		callbackToggledToBean();
	}
	
	private void callbackToggledToBean()
	{
		try {if(bean!=null){bean.filtered(this);}}
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