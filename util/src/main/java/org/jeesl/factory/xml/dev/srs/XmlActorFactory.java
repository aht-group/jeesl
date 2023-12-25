package org.jeesl.factory.xml.dev.srs;

import org.jeesl.model.xml.dev.srs.Actor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlActorFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlActorFactory.class);
		
    public static Actor build(String code, String name)
    {
    	Actor xml = new Actor();
    	xml.setCode(code);
    	xml.setName(name);
    	return xml;
    }
}