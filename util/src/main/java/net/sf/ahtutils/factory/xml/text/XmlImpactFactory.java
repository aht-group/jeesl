package net.sf.ahtutils.factory.xml.text;

import org.jeesl.model.xml.io.cms.chars.Impact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlImpactFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlImpactFactory.class);
	
	public static Impact build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Impact build(E key, String value){return build(key.toString(),value);}
	public static Impact build(String key,String value) {return build(key,null,value);}
	
	public static Impact build(String key,Integer version,String value)
	{
		Impact xml = new Impact();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}