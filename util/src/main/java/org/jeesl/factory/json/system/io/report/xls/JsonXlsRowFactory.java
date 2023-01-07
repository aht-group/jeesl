package org.jeesl.factory.json.system.io.report.xls;

import org.jeesl.model.json.io.report.xlsx.JsonXlsColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonXlsRowFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonXlsRowFactory.class);
	
	public static JsonXlsColumn build(){return new JsonXlsColumn();}
}