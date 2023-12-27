package net.sf.ahtutils.factory.xml.text;


import org.jeesl.model.xml.io.cms.chars.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlQuestionFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlQuestionFactory.class);
	
	public static Question build(String value) {return build(null,null,value);}
	public static Question build(String key,String value) {return build(key,null,value);}
	
	public static Question build(String key,Integer version,String value)
	{
		Question xml = new Question();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}