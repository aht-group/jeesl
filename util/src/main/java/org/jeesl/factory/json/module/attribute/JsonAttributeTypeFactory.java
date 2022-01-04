package org.jeesl.factory.json.module.attribute;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.module.attribute.JsonAttributeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAttributeTypeFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonAttributeTypeFactory.class);
	
	private final String localeCode;
	private final JsonAttributeType q;
	
	public JsonAttributeTypeFactory(JsonAttributeType q) {this(null,q);}
	public JsonAttributeTypeFactory(String localeCode, JsonAttributeType q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static JsonAttributeType build() {return new JsonAttributeType();}
	public static <E extends Enum<E>> JsonAttributeType build(E code) {return JsonAttributeTypeFactory.build(code.toString());}
	public static JsonAttributeType build(String code) {JsonAttributeType json=build(); json.setCode(code); return json;}
		
	public JsonAttributeType build(S ejb)
	{
		JsonAttributeType json = new JsonAttributeType();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.getCode()!=null){json.setCode(ejb.getCode());}
		if(q.getLabel()!=null && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.getDescription()!=null && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}