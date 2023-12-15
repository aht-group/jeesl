package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Achievement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlAchievementFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlAchievementFactory.class);
	
	public static Achievement build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Achievement build(E key, String value){return build(key.toString(),value);}
	public static Achievement build(String key,String value) {return build(key,null,value);}
	
	public static Achievement build(String key,Integer version,String value)
	{
		Achievement xml = new Achievement();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}