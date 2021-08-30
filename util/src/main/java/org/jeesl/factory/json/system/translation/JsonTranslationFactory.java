package org.jeesl.factory.json.system.translation;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.model.json.system.translation.JsonTranslation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTranslationFactory <RE extends JeeslRevisionEntity<?,?,?,?,RA,?>, RA extends JeeslRevisionAttribute<?,?,RE,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTranslationFactory.class);
	
	private final String localeCode;
	
	public static <RE extends JeeslRevisionEntity<?,?,?,?,RA,?>, RA extends JeeslRevisionAttribute<?,?,RE,?,?>>
		JsonTranslationFactory<RE,RA> instance(String localeCode) {return new JsonTranslationFactory<>(localeCode);}
	
	private JsonTranslationFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public static <E extends Enum<E>> JsonTranslation build(Class<?> c, E attribute, String xpath, String filterBy)
	{
		JsonTranslation json = build(c,attribute,xpath);
		json.setFilterBy(filterBy);
		return json;
	}
	
	public static <E extends Enum<E>> JsonTranslation build(Class<?> c, E attribute, String xpath)
	{
		JsonTranslation json = new JsonTranslation();
		json.setEntity(c.getSimpleName());
		if(attribute!=null) {json.setCode(attribute.toString());}
		json.setXpath(xpath);
		return json;
	}
	
	public static JsonTranslation build(String code, String label)
	{
		JsonTranslation json = new JsonTranslation();
		json.setCode(code);
		json.setLabel(label);
		return json;
	}
	
	public List<JsonTranslation> labels(RE entity)
	{
		List<JsonTranslation> list = new ArrayList<>();
		for(RA attribute : entity.getAttributes())
		{
			list.add(JsonTranslationFactory.build(attribute.getCode(),attribute.getName().get(localeCode).getLang()));
		}
		return list;
	}
}