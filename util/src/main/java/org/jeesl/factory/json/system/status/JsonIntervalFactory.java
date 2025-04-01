package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonIntervalFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonIntervalFactory.class);
	
	private final String localeCode;
	private final JsonInterval q;
	
	public JsonIntervalFactory(JsonInterval q) {this(null,q);}
	public JsonIntervalFactory(String localeCode, JsonInterval q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static JsonInterval build() {return new JsonInterval();}
	public static <E extends Enum<E>> JsonInterval build(E code){return JsonIntervalFactory.build(code.toString());}
	public static <E extends Enum<E>> JsonInterval build(String code){JsonInterval json = build(); json.setCode(code.toString()); return json;}
	
	public JsonInterval build(S ejb)
	{
		JsonInterval json = new JsonInterval();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.getCode()!=null){json.setCode(ejb.getCode());}
		if(q.getLabel()!=null && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.getDescription()!=null && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}