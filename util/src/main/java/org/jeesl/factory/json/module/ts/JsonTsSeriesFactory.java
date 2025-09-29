package org.jeesl.factory.json.module.ts;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.model.json.module.ts.JsonTsData;
import org.jeesl.model.json.module.ts.JsonTsSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTsSeriesFactory<DATA extends JeeslTsData<?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTsSeriesFactory.class);
	
	private JsonTsSeries json;
	
	public JsonTsSeriesFactory()
	{
		this.clear();
	}
	
	public JsonTsSeriesFactory<DATA> clear() {json = JsonTsSeriesFactory.build(); return this;}
	public JsonTsSeriesFactory<DATA> add(JsonTsData data) {if(Objects.isNull(json.getDatas())) {json.setDatas(new ArrayList<>());} json.getDatas().add(data); return this;}
	public JsonTsSeries assemble() {return json;}
	
	public static JsonTsSeries build() {return new JsonTsSeries();}
	
	public static void updateDateRange(JsonTsSeries series, Date record)
	{
		if(series.getDateStart()==null) {series.setDateStart(record);}
		else if(record.before(series.getDateStart())){series.setDateStart(record);}
		
		if(series.getDateEnd()==null) {series.setDateEnd(record);}
		else if(record.after(series.getDateEnd())){series.setDateEnd(record);}
	}
}