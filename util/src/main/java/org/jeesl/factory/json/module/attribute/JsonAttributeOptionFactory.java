package org.jeesl.factory.json.module.attribute;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.module.attribute.JsonAttributeOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAttributeOptionFactory<L extends JeeslLang, D extends JeeslDescription, OPTION extends JeeslAttributeOption<L,D,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonAttributeOptionFactory.class);
	
	private final String localeCode;
	private final JsonAttributeOption q;
	
	public JsonAttributeOptionFactory(String localeCode, JsonAttributeOption q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static JsonAttributeOption build(String code, String label)
	{
		JsonAttributeOption json = new JsonAttributeOption();
		json.setCode(code);
		json.setLabel(label);
		return json;
	}
		
	public JsonAttributeOption build(OPTION ejb)
	{
		JsonAttributeOption json = new JsonAttributeOption();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.getCode()!=null){json.setCode(ejb.getCode());}
		if(q.getPosition()!=null) {json.setPosition(ejb.getPosition());}
		if(q.getLabel()!=null && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.getDescription()!=null && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}