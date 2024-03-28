package org.jeesl.controller.handler.th;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.bean.TableMultiSelectionBean;

@Deprecated //Use ThMultiFilter instead
public class TableMultiSelectionHandler <T extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(TableMultiSelectionHandler.class);

	private TableMultiSelectionBean callback; public TableMultiSelectionBean getCallback() {return callback;} public void setCallback(TableMultiSelectionBean callback) {this.callback = callback;}
	
	protected List<T> entities; public List<T> getEntities() {return entities;}
	private T entity; public T getEntity() {return entity;} public void setEntity(T entity) {this.entity = entity;}
	private Map<T,Boolean> map; public Map<T,Boolean> getMap(){return map;}

	public TableMultiSelectionHandler()
	{
		this(new ArrayList<T>());
	}
	
	public TableMultiSelectionHandler(List<T> list)
	{
		map = new ConcurrentHashMap<T,Boolean>();
		entities = list;
		for(T type : list)
		{
			map.put(type,false);
		}
	}
	
	
	public void preSelect(T type)
	{
		List<T> types = new ArrayList<T>();
		types.add(type);
		preSelect(types);
	}
	public void preSelect(List<T> types)
	{
		map.clear();
		for(T type : types)
		{
			map.put(type,true);
		}
	}

	public void select()
	{
		logger.info(AbstractLogMessage.selectEntity(entity)+": "+map.get(entity));
		if (map.containsKey(entity)) {map.put(entity, !map.get(entity));}
		else {map.put(entity, true);}
		if(callback!=null){callback.callbackTableMultiSelection();}
	}

	public List<T> toSelected()
	{
		List<T> types = new ArrayList<T>();
		for(T type : map.keySet())
		{
			if(map.get(type))
			{
				types.add(type);
			}
		}
		return types;
	}
}
