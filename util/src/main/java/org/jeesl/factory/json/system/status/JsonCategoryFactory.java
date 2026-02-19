package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonCategoryFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonCategoryFactory.class);
	
	private final String localeCode;
	private final JsonCategory q;
	
	public static <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>> JsonCategoryFactory<L,D,S> instance(String localeCode, JsonCategory q)
	{
		return new JsonCategoryFactory<>(localeCode,q);
	}
	public JsonCategoryFactory(String localeCode, JsonCategory q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
		
	public static JsonCategory build(String code, String label)
	{
		JsonCategory json = build(code);
		json.setLabel(label);
		return json;
	}
	public static <E extends Enum<E>> JsonCategory build(E code) {return JsonCategoryFactory.build(code.toString());}
	public static JsonCategory build(String code)
	{
		JsonCategory json = new JsonCategory();
		json.setCode(code);
		return json;
	}
	
	public JsonCategory build(S ejb)
	{
		JsonCategory json = new JsonCategory();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetLabel() && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.isSetDescription() && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}