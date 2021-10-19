package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTopicFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTopicFactory.class);
	
	private final String localeCode;
	private final JsonTopic q;
	
	public JsonTopicFactory(String localeCode, JsonTopic q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
		
	public static JsonTopic build(String code, String label)
	{
		JsonTopic json = build(code);
		json.setLabel(label);
		return json;
	}
	public static JsonTopic build(String code)
	{
		JsonTopic json = new JsonTopic();
		json.setCode(code);
		return json;
	}
	
	public JsonTopic build(S ejb)
	{
		JsonTopic json = new JsonTopic();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.getLabel()!=null && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.getDescription()!=null && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}