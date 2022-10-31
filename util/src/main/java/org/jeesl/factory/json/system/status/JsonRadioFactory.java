package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonRadio;
import org.jeesl.model.json.system.status.JsonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonRadioFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonRadioFactory.class);
	
	private final String localeCode;
	private final JsonRadio q;
	
	public JsonRadioFactory(JsonRadio q) {this(null,q);}
	public JsonRadioFactory(String localeCode, JsonRadio q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static JsonRadio build() {return new JsonRadio();}
	public static <E extends Enum<E>> JsonRadio build(E code) {return build(code.toString());}
	public static JsonRadio build(String code) {JsonRadio json=build(); json.setCode(code); return json;}
		
	public JsonRadio build(S ejb)
	{
		JsonRadio json = new JsonRadio();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.getCode()!=null){json.setCode(ejb.getCode());}
		if(q.getLabel()!=null && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.getDescription()!=null && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}