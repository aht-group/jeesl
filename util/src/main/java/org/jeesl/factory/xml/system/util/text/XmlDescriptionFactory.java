package org.jeesl.factory.xml.system.util.text;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.model.xml.io.cms.text.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDescriptionFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlDescriptionFactory.class);
	
	public static Description build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Description build(E key, String value){return build(key.toString(),value);}
	public static Description build(String key, String value) {return build(key,null,value);}
	
	public static Description build(String key, Integer version, String value)
	{
		Description xml = new Description();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
	
	public static Description build(List<String> values, String join) {return build(null,null,StringUtils.join(values,join));}
}