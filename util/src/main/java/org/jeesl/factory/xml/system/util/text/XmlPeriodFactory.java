package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlPeriodFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlPeriodFactory.class);
	
	public static Period build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Period build(E key, String value){return build(key.toString(),value);}
	public static Period build(String key,String value) {return build(key,null,value);}
	
	public static Period build(String key,Integer version,String value)
	{
		Period xml = new Period();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}