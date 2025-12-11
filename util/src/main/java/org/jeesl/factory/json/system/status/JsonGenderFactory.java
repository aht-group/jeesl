package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonGender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonGenderFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonGenderFactory.class);
	
	private final String localeCode;
	private final JsonGender q;
	
	public JsonGenderFactory(String localeCode, JsonGender q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public JsonGender build(S ejb)
	{
		JsonGender json = new JsonGender();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetLabel() && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.isSetDescription() && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
	
	public static <E extends Enum<E>> JsonGender build(E code)
	{
		JsonGender json = new JsonGender();
		json.setCode(code.toString());
		return json;
	}
	public static JsonGender build(String code, String label)
	{
		JsonGender json = new JsonGender();
		json.setCode(code);
		json.setLabel(label);
		return json;
	}
}