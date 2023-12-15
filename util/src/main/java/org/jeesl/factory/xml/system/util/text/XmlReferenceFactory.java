package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlReferenceFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlReferenceFactory.class);
	
	public static Reference build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Reference build(E key, String value){return build(key.toString(),value);}
	public static Reference build(String key,String value) {return build(key,null,value);}
	
	public static Reference build(String key,Integer version,String value)
	{
		Reference xml = new Reference();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}