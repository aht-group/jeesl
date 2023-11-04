package org.jeesl.factory.json.util.query;

import org.exlp.util.io.JsonUtil;
import org.jeesl.model.json.util.query.JsonFilter;
import org.jeesl.model.json.util.query.JsonFilterItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.sf.exlp.util.io.HashUtil;

public class JsonFilterFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonFilterFactory.class);
	
	public static JsonFilter build(String localeCode) 
	{
		JsonFilter json = new JsonFilter();
		json.setLocaleCode(localeCode);
		return json;
	}
	
	public static void add(JsonFilter filter, JsonFilterItem item)
	{
		filter.getItems().add(item);
	}
	
	public static String hash(JsonFilter filter)
	{
		try {
			return HashUtil.hash(JsonUtil.toString(filter));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "--";
	}
}