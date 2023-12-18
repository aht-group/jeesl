package net.sf.ahtutils.factory.xml.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.text.Title;

public class XmlTitleFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlTitleFactory.class);
	
	public static Title build(String value) {return build(null,null,value);}
	public static Title build(String key,String value) {return build(key,null,value);}
	
	public static Title build(String key,Integer version,String value)
	{
		Title xml = new Title();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}