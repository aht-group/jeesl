package org.jeesl.factory.json.system.io.db;

import org.jeesl.model.json.io.db.pg.JsonDateTruncation;


public class Json1DateTruncationFactory
{
	private JsonDateTruncation json;
	
	public static Json1DateTruncationFactory instance() {return new Json1DateTruncationFactory();}
	private  Json1DateTruncationFactory()
	{
		json = Json1DateTruncationFactory.build();
	}
	
	public Json1DateTruncationFactory attribute(String attribute) {json.setAttribute(attribute); return this;}
	public Json1DateTruncationFactory interval(String interval) {json.setInterval(interval); return this;}
	public Json1DateTruncationFactory alias(String alias) {json.setAlias(alias); return this;}
	
	public JsonDateTruncation assemble() {return json;}

	public static JsonDateTruncation build() {return new JsonDateTruncation();}
}