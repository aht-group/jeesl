package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTeamFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlTeamFactory.class);
	
	public static Team build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Team build(E key, String value){return build(key.toString(),value);}
	public static Team build(String key,String value) {return build(key,null,value);}
	
	public static Team build(String key,Integer version,String value)
	{
		Team xml = new Team();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
}