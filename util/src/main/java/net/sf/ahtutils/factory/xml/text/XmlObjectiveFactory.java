package net.sf.ahtutils.factory.xml.text;

import org.jeesl.model.xml.io.cms.chars.Objective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlObjectiveFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlObjectiveFactory.class);
	
	public static Objective build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Objective build(E key, String value){return build(key.toString(),value);}
	public static Objective build(String key,String value) {return build(key,null,value);}
	
	public static Objective build(String key,Integer version,String value)
	{
		Objective xml = new Objective();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}