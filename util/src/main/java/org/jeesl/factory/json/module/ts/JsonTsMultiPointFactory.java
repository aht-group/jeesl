package org.jeesl.factory.json.module.ts;

import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.model.json.module.ts.JsonTsMultipoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTsMultiPointFactory<MP extends JeeslTsMultiPoint<?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTsMultiPointFactory.class);
	
	private final String localeCode;
	private final JsonTsMultipoint q;
	
	public JsonTsMultiPointFactory(JsonTsMultipoint q) {this(null,q);}
	public JsonTsMultiPointFactory(String localeCode, JsonTsMultipoint q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
		
	public JsonTsMultipoint build(MP ejb)
	{
		JsonTsMultipoint json = new JsonTsMultipoint();
	
		if(q.isSetId()){json.setId(ejb.getId());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetLabel() && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.isSetDescription() && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}