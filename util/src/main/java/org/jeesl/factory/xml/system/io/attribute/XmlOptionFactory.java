package org.jeesl.factory.xml.system.io.attribute;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.system.io.attribute.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlOptionFactory <L extends JeeslLang, D extends JeeslDescription,
									
									OPTION extends JeeslAttributeOption<L,D,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlOptionFactory.class);
	
	private final String localeCode;
	
	public XmlOptionFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public static Option build(){return new Option();}

	
	public Option build(OPTION option)
	{
		Option xml = build();
		xml.setCode(option.getCode());
		xml.setLabel(option.getName().get(localeCode).getLang());
		return xml;
	}
	
	public static Option build(boolean value)
	{
		Option xml = build();
		xml.setCode(Boolean.valueOf(value).toString());
		xml.setLabel(Boolean.valueOf(value).toString());
		return xml;
	}
	
	public static Option build(String code, String label)
	{
		Option xml = build();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}