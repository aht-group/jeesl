package org.jeesl.factory.xml.dev.srs;

import org.jeesl.model.xml.dev.srs.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlModuleFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlModuleFactory.class);
		
    public static Module build(String code, String name)
    {
    	Module xml = new Module();
    	xml.setCode(code);
    	xml.setLabel(name);
    	return xml;
    }
}