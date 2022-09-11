package org.jeesl.factory.xml.system.security;

import org.jeesl.model.xml.system.security.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlRuleFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlRuleFactory.class);
		
	public static Rule build(boolean valid, String type, String code, Integer min, Integer max, Integer acutal)
	{
		Rule xml = new Rule();
		xml.setValid(valid);
		xml.setType(type);
		xml.setCode(code);
		if(min!=null){xml.setMin(min);}
		if(max!=null){xml.setMax(max);}
		if(acutal!=null){xml.setActual(acutal);}
		return xml;
	}
}