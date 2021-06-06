package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTypeFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTypeFactory.class);
	
	private final String localeCode;
	private final JsonType q;
	
	public JsonTypeFactory(JsonType q) {this(null,q);}
	public JsonTypeFactory(String localeCode, JsonType q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
		
	public JsonType build(S ejb)
	{
		JsonType json = new JsonType();
	
		if(q.isSetId()){json.setId(ejb.getId());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetLabel() && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.isSetDescription() && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}