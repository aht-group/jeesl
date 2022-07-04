package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonSector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSectorFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSectorFactory.class);
	
	private final String localeCode;
	private final JsonSector q;
	
	public static <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>> JsonSectorFactory<L,D,S> instance(String localeCode, JsonSector q)
	{
		return new JsonSectorFactory<>(localeCode,q);
	}
	
	public JsonSectorFactory(String localeCode, JsonSector q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
		
	public static JsonSector build(String code, String label)
	{
		JsonSector json = new JsonSector();
		json.setCode(code);
		json.setLabel(label);
		return json;
	}
	
	public JsonSector build(S ejb)
	{
		JsonSector json = new JsonSector();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetLabel() && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.isSetDescription() && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}