package org.jeesl.factory.json.module.calendar;

import java.util.Objects;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.module.calendar.JsonCalendarTimezone;
import org.jeesl.model.json.system.status.JsonScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTimezoneFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTimezoneFactory.class);
	
	private final String localeCode;
	private final JsonScope q;
	
	public static <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>> JsonTimezoneFactory<L,D,S> instance(String localeCode, JsonScope q)
	{
		return new JsonTimezoneFactory<>(localeCode,q);
	}
	public JsonTimezoneFactory(String localeCode, JsonScope q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	
	public static JsonCalendarTimezone build() {return new JsonCalendarTimezone();}
	public static JsonCalendarTimezone build(String code) {JsonCalendarTimezone json = JsonTimezoneFactory.build(); json.setCode(code); return json;}
	public static JsonCalendarTimezone build(String code, String label)
	{
		JsonCalendarTimezone json = JsonTimezoneFactory.build();
		json.setCode(code);
		json.setLabel(label);
		return json;
	}
	
	public JsonCalendarTimezone build(S ejb)
	{
		JsonCalendarTimezone json = JsonTimezoneFactory.build();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())){json.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getLabel()) && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(Objects.nonNull(q.getDescription()) && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}