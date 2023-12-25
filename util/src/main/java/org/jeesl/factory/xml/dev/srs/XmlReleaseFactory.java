package org.jeesl.factory.xml.dev.srs;

import org.jeesl.model.xml.dev.srs.Release;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlReleaseFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlReleaseFactory.class);
		
    public static Release build(String code, String name)
    {
    	Release xml = new Release();
    	xml.setCode(code);
    	xml.setName(name);
    	return xml;
    }
}