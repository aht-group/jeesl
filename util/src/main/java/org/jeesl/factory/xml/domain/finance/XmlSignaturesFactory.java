package org.jeesl.factory.xml.domain.finance;

import org.jeesl.model.xml.module.finance.Signatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSignaturesFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlSignaturesFactory.class);
	
    public static Signatures build()
    {
    	Signatures xml = new Signatures();
    	return xml;
    }
    
    public static Signatures build(String code)
    {
    	Signatures xml = build();
    	xml.setCode(code);
    	return xml;
    }
    
    public static Signatures build(String code, String label)
    {
    	Signatures xml = build(code);
    	xml.setLabel(label);
    	return xml;
    }
}