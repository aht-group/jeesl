package org.jeesl.factory.json.util.query;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.util.query.JsonFilter;
import org.jeesl.model.json.util.query.JsonFilterItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonFilterItemFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonFilterItemFactory.class);
	
	public static <T extends EjbWithId> JsonFilterItem build(Class<T> c, JsonFilter.Type type, List<T> list) 
	{
		JsonFilterItem json = new JsonFilterItem();
		json.setClassName(c.getName());
		json.setType(type.toString());
		json.setIds(EjbIdFactory.toLongs(list));
		Collections.sort(json.getIds());
		return json;
	}
	
	public static <T extends EjbWithId> JsonFilterItem build(T ejb) 
	{
		JsonFilterItem json = new JsonFilterItem();
		json.setClassName(ejb.getClass().getName());
		json.setType(JsonFilter.Type.idSingle.toString());
		json.setIds(EjbIdFactory.toIds(ejb));
		Collections.sort(json.getIds());
		return json;
	}
	
	public static JsonFilterItem build(String code, Date date) 
	{
		JsonFilterItem json = new JsonFilterItem();
		json.setClassName(date.getClass().getName());
		json.setType(code);
		json.setDate(date);
		return json;
	}
}