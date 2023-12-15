package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Challenge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlChallengeFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlChallengeFactory.class);
	
	public static Challenge build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Challenge build(E key, String value){return build(key.toString(),value);}
	public static Challenge build(String key,String value) {return build(key,null,value);}
	
	public static Challenge build(String key,Integer version,String value)
	{
		Challenge xml = new Challenge();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}