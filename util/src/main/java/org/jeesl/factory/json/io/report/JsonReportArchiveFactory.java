package org.jeesl.factory.json.io.report;

import org.jeesl.model.json.io.fr.JsonFrFile;
import org.jeesl.model.json.io.report.JsonReportArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonReportArchiveFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonReportArchiveFactory.class);
	
	public static JsonReportArchive build(){return new JsonReportArchive();}
	
	public static JsonReportArchive build(long id, JsonFrFile file)
	{
		JsonReportArchive json = build();
		json.setId(id);
		json.setFile(file);
		return json;
	}
}