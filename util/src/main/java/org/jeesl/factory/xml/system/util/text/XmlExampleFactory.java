package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlExampleFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlExampleFactory.class);
	
	public static Example build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Example build(E key, String value){return build(key.toString(),value);}
	public static Example build(String key,String value) {return build(key,null,value);}
	
	public static Example build(String key, Integer version,String value)
	{
		Example xml = new Example();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}