package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Finding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFindingFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlFindingFactory.class);
	
	public static Finding build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Finding build(E key, String value){return build(key.toString(),value);}
	public static Finding build(String key,String value) {return build(key,null,value);}
	
	public static Finding build(String key,Integer version,String value)
	{
		Finding xml = new Finding();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}