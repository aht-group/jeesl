package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLocationFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlLocationFactory.class);
	
	public static Location build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Location build(E key, String value){return build(key.toString(),value);}
	public static Location build(String key,String value) {return build(key,null,value);}
	
	public static Location build(String key,Integer version,String value)
	{
		Location xml = new Location();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}