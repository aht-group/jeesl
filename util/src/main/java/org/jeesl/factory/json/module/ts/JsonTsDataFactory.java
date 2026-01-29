package org.jeesl.factory.json.module.ts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import org.exlp.util.system.DateUtil;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.model.json.module.ts.JsonTsData;
import org.jeesl.model.json.module.ts.JsonTsPoint;
import org.jeesl.model.json.module.ts.JsonTsSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTsDataFactory<DATA extends JeeslTsData<?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTsDataFactory.class);
	
	private final JsonTsData q;
	private JsonTsData json;
	
	public JsonTsDataFactory(JsonTsData q)
	{
		this.q=q;
		this.clear();
	}
		
	public JsonTsData build(DATA ejb)
	{
		JsonTsData json = new JsonTsData();
	
		if(q.isSetId()){json.setId(ejb.getId());}
		if(Objects.nonNull(q.getLocalDateTime())){json.setLocalDateTime(DateUtil.toLocalDateTime(ejb.getRecord()));}
		if(Objects.nonNull(q.getValue())) {json.setValue(ejb.getValue());}
		
		return json;
	}
	
	public static JsonTsData build(LocalDateTime time, double value)
	{
		JsonTsData json = new JsonTsData();
	
		json.setLocalDateTime(time);
		json.setValue(value);
		
		return json;
	}
	public JsonTsDataFactory<DATA> timestamp(LocalDateTime time) {json.setLocalDateTime(time); return this;}
	public JsonTsDataFactory<DATA> info(String info) {json.setVbaRecord(info); return this;}
	public JsonTsDataFactory<DATA> clear() {json = JsonTsDataFactory.build(); return this;}
	public JsonTsDataFactory<DATA> add(JsonTsPoint point) {if(Objects.isNull(json.getPoints())) {json.setPoints(new ArrayList<>());} json.getPoints().add(point); return this;}
	public JsonTsData assemble() {return json;}
	
	public static JsonTsData build() {return new JsonTsData();}
	public static void append(JsonTsSeries ts, LocalDateTime ldt, Double value)
	{
		if(Objects.isNull(ts.getDatas())) {ts.setDatas(new ArrayList<>());}
		
		JsonTsData data = new JsonTsData();
		data.setLocalDateTime(ldt);
		data.setValue(value);
		ts.getDatas().add(data);
	}
	
	public static JsonTsSeries toSeries(JsonTsData data)
	{
		JsonTsSeries json = data.getSeries();
		data.setSeries(null);
		json.setDatas(new ArrayList<JsonTsData>());
		json.getDatas().add(data);
		return json;
	}
}