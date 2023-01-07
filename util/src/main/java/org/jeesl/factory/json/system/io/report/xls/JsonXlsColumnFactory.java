package org.jeesl.factory.json.system.io.report.xls;

import org.jeesl.model.json.io.report.xlsx.JsonXlsColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonXlsColumnFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonXlsColumnFactory.class);
	
	public static JsonXlsColumn build(){return new JsonXlsColumn();}
	
	public static JsonXlsColumn build(String code)
	{
		JsonXlsColumn json = build();
		json.setCode(code);
		return json;
	}
	
	
}