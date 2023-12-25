package org.jeesl.factory.xml.dev.srs;

import org.jeesl.model.xml.dev.srs.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlVersionFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlVersionFactory.class);
		
    public static Version build(String code)
    {
    	Version xml = new Version();
    	xml.setCode(code);
    	return xml;
    }
}