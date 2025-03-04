package org.jeesl.jsf.handler.th;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.StringUtil;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.th.ThMultiFilter;
import org.jeesl.interfaces.bean.th.ThMultiFilterBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThMultiFilterHandler <T extends EjbWithGraphic<?>> implements ThMultiFilter
{
	final static Logger logger = LoggerFactory.getLogger(ThMultiFilterHandler.class);
	private static final long serialVersionUID = 1L;

	private final ThMultiFilterBean bean;

	private final List<T> list; public List<T> getList() {return list;} public void setList(List<T> list) {this.list.clear(); this.list.addAll(list);}
	private final List<T> selected; public List<T> getSelected() {return selected;}
	private final List<T> selection; public List<T> getSelection() {return selection;}
	
	private T selectedSingle; public T getSelectedSingle() {return selectedSingle;} public void setSelectedSingle(T value) {selectedSingle = value;}
	
	private final List<T> memory;
	private final Map<T,Boolean> map; public Map<T,Boolean> getMap() {return map;}
	public boolean toggleMode; public boolean isToggleMode() {return toggleMode;} public void setToggleMode(boolean toggleMode) {this.toggleMode = toggleMode;}
	private int selectedSize; public int getSelectedSize() {return selectedSize;}

	public static <T extends EjbWithGraphic<?>> ThMultiFilterHandler<T> instance(ThMultiFilterBean callback) {return new ThMultiFilterHandler<>(callback);}
	public ThMultiFilterHandler(ThMultiFilterBean bean)
	{
		this.bean=bean;
		list = new ArrayList<>();
		map = new ConcurrentHashMap<T,Boolean>();
		selected = new ArrayList<>();
		selection = new ArrayList<>();
		memory = new ArrayList<>();
		toggleMode = false;
		refresh();
	}
	
	public ThMultiFilterHandler<T> toggleMode(boolean toggleMode) {this.setToggleMode(toggleMode); return this;}

	public void clear()
	{
		list.clear();
		selected.clear();
		map.clear();
	}

	@SuppressWarnings("unchecked")
	public <E extends Enum<E>, S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription> void add(JeeslFacade fUtils, Class<S> c, E code)
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
		this.refresh();
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
	public void preSelect(T selection)
	{
		map.put(selection,true);
		refresh();
	}
	public void preSelect(List<T> selection)
	{
		if(ObjectUtils.isNotEmpty(selection))
		{
			for(T t : selection)
			{
				map.put(t,true);
			}
			this.refresh();
		}
	}
	public void preSelectOrAll(List<T> selection)
	{
		if(ObjectUtils.isNotEmpty(selection)) {this.preSelect(selection);}
		else {selectAll();}
	}
	
	public void select(T t)
	{
		map.put(t,true);
		refresh();
	}

	public void deselectAll() {for(T t : list) {map.put(t,false);}refresh();}
	public void deselect(T t)
	{
		map.put(t,false);
		refresh();
	}

	// Copies selected Items to Memory
	public void memoryUpdate()
	{
		memory.clear();
		memory.addAll(selected);
	}

	public void memoryApply()
	{
		map.clear();
		for(T t : list){map.put(t,false);}
		for(T t : memory) {map.put(t,true);}
		this.refresh();
	}

	public void toggle(T type)
	{
		logger.info("Toggle "+type);
		if(toggleMode)
		{
			deselectAll();
			logger.info("toggleMode : " + toggleMode);
		}
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
		selection.clear();
		selected.clear();
		for(T t : list)
		{
			if(!map.containsKey(t)) {map.put(t,false);}
			if(map.get(t)){selected.add(t);}
		}
		selection.addAll(selected);
		Collections.reverse(selection);
		
		selectedSize = selected.size();
		if(selectedSize==1) {selectedSingle = selection.get(0);}
		else {selectedSingle=null;}
	}

	public boolean isSelected(T t)
	{
		return map.containsKey(t) && map.get(t);
	}

	public boolean getHasMore() {return list.size()>1;}
	public boolean getHasNone(){return list.isEmpty();}
	public boolean getHasOne() {return list.size()==1;}
	public boolean getHasSome(){return !list.isEmpty();}
	public boolean getHasSelected(){return hasSelected();}
	public boolean getHasOneSelected(){return selected.size()==1;}
	public boolean getHasMoreSelected(){return selected.size()>1;}
	public boolean getHasSomeSelected(){return !selected.isEmpty() && !allSelected();}
	public boolean hasSelected(){return !selected.isEmpty();}
	public boolean allSelected(){return selected.size()==list.size();}

	public boolean isSelectedA() {if(list.size()==0) {return false;} else {return isSelected(0);}}
	public boolean isSelectedB() {if(list.size()<1) {return false;} else {return isSelected(1);}}
	public boolean isSelectedC() {if(list.size()<2) {return false;} else {return isSelected(2);}}
	public boolean isSelectedD() {if(list.size()<3) {return false;} else {return isSelected(3);}}
	public boolean isSelectedE() {if(list.size()<4) {return false;} else {return isSelected(4);}}
	private boolean isSelected(int index) {return map.containsKey(list.get(index)) && map.get(list.get(index));}

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