package net.sf.ahtutils.factory.xml.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.text.Answer;

public class XmlAnswerFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlAnswerFactory.class);
	
	public static Answer build(String value) {return build(null,null,value);}
	public static Answer build(String key,String value) {return build(key,null,value);}
	
	public static Answer build(String key,Integer version,String value)
	{
		Answer xml = new Answer();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}