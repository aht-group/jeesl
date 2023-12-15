package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLabelFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlLabelFactory.class);
	
	public static Label build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Label build(E key, String value){return build(key.toString(),value);}
	public static Label build(String key,String value) {return build(key,null,value);}
	
	public static Label build(String key,Integer version,String value)
	{
		Label xml = new Label();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}