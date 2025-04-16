package org.jeesl.factory.json.module.ts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import org.exlp.util.system.DateUtil;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.model.json.module.ts.JsonTsData;
import org.jeesl.model.json.module.ts.JsonTsSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTsDataFactory<DATA extends JeeslTsData<?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTsDataFactory.class);
	
	private final JsonTsData q;
	
	public JsonTsDataFactory(JsonTsData q)
	{
		this.q=q;
	}
		
	public JsonTsData build(DATA ejb)
	{
		JsonTsData json = new JsonTsData();
	
		if(q.isSetId()){json.setId(ejb.getId());}
		if(Objects.nonNull(q.getLocalDateTime())){json.setLocalDateTime(DateUtil.toLocalDateTime(ejb.getRecord()));}
		
		return json;
	}
	
	public static JsonTsData build(LocalDateTime time, double value)
	{
		JsonTsData json = new JsonTsData();
	
		json.setLocalDateTime(time);
		json.setValue(value);
		
		return json;
	}
	
	public static void append(JsonTsSeries ts, LocalDateTime ldt, Double value)
	{
		if(Objects.isNull(ts.getDatas())) {ts.setDatas(new ArrayList<>());}
		
		JsonTsData data = new JsonTsData();
		data.setLocalDateTime(ldt);
		data.setValue(value);
		ts.getDatas().add(data);
	}
}