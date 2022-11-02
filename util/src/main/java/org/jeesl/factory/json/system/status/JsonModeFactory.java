package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonModeFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonModeFactory.class);
	
	private final String localeCode;
	private final JsonMode q;
	
	public static <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>> JsonModeFactory<L,D,S> instance(String localeCode, JsonMode q)
	{
		return new JsonModeFactory<>(localeCode,q);
	}
	private JsonModeFactory(String localeCode, JsonMode q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
		
	public static JsonMode build(String code, String label)
	{
		JsonMode json = build(code);
		json.setLabel(label);
		return json;
	}
	public static JsonMode build(String code)
	{
		JsonMode json = new JsonMode();
		json.setCode(code);
		return json;
	}
	
	public JsonMode build(S ejb)
	{
		JsonMode json = new JsonMode();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetLabel() && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.isSetDescription() && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}