package org.jeesl.factory.json.module.ts;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import org.jeesl.model.json.module.ts.JsonTsContainer;
import org.jeesl.model.json.module.ts.JsonTsScope;
import org.jeesl.model.json.module.ts.JsonTsSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTsContainerFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonTsContainerFactory.class);
	
	private JsonTsContainer json;
	
	public static JsonTsContainerFactory instance() {return new JsonTsContainerFactory();}
	private JsonTsContainerFactory()
	{
		this.clear();
	}
	
	public JsonTsContainerFactory clear() {json = JsonTsContainerFactory.build(); return this;}
	public JsonTsContainerFactory add(JsonTsScope scope) {if(Objects.isNull(json.getScopes())) {json.setScopes(new ArrayList<>());} json.getScopes().add(scope); return this;}
	public JsonTsContainer assemble() {return json;}
	
	public static JsonTsContainer build() {return new JsonTsContainer();}
	
	public static void updateDateRange(JsonTsSeries series, Date record)
	{
		if(series.getDateStart()==null) {series.setDateStart(record);}
		else if(record.before(series.getDateStart())){series.setDateStart(record);}
		
		if(series.getDateEnd()==null) {series.setDateEnd(record);}
		else if(record.after(series.getDateEnd())){series.setDateEnd(record);}
	}
}