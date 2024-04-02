package org.jeesl.jsf.handler.th;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.interfaces.bean.th.ThBooleanBean;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableBooleanSelectionHandler <T extends EjbWithId> implements ThBooleanBean
{
	final static Logger logger = LoggerFactory.getLogger(TableBooleanSelectionHandler.class);
	private static final long serialVersionUID = 1L;

	private final ThBooleanBean bean;

	private final Map<T,Boolean> map;  public Map<T, Boolean> getMap() {return map;}
	
	public static <T extends EjbWithId> TableBooleanSelectionHandler<T> instance(ThBooleanBean callback) {return new TableBooleanSelectionHandler<>(callback);}
	public TableBooleanSelectionHandler(ThBooleanBean bean)
	{
		this.bean=bean;
		
		map = new HashMap<T,Boolean>();
	}
	
	public void clear()
	{
		map.clear();
	}

	public void toggle(T t)
	{
		if(!map.containsKey(t)) {map.put(t,true);}
		else
		{
			map.put(t,!map.get(t));
		}
		logger.info("Toggled "+t+" "+map.get(t));
	}
}