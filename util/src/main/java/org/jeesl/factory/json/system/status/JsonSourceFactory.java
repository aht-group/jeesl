package org.jeesl.factory.json.system.status;

import java.util.Objects;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSourceFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSourceFactory.class);
	
	private final String localeCode;
	private final JsonSource q;
	
	public static <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>> JsonSourceFactory<L,D,S> instance(String localeCode, JsonSource q)
	{
		return new JsonSourceFactory<>(localeCode,q);
	}
	
	public JsonSourceFactory(String localeCode, JsonSource q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static JsonSource build(String code)
	{
		JsonSource json = new JsonSource();
		json.setCode(code);
		return json;
	}
	public static JsonSource build(String code, String label)
	{
		JsonSource json = new JsonSource();
		json.setCode(code);
		json.setLabel(label);
		return json;
	}
	
	public JsonSource build(S ejb)
	{
		JsonSource json = new JsonSource();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())){json.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getLabel()) && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(Objects.nonNull(q.getDescription()) && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}