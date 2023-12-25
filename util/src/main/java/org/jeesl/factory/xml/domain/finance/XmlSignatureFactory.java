package org.jeesl.factory.xml.domain.finance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Signature;

public class XmlSignatureFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlSignatureFactory.class);
	
    public static Signature build()
    {
    	Signature xml = new Signature();
    	return xml;
    }
    
    public static Signature build(String code)
    {
    	Signature xml = build();
    	xml.setCode(code);
    	return xml;
    }
    
    public static Signature build(int position)
    {
    	Signature xml = build();
    	xml.setPosition(position);
    	return xml;
    }
    
    public static Signature build(String code, String label){return build(null,code,label);}
    public static Signature build(Integer position, String label){return build(position,null,label);}
    public static Signature build(Integer position, String code, String label)
    {
    	Signature xml = build(code);
    	xml.setLabel(label);
    	if(position!=null) {xml.setPosition(position);}
    	return xml;
    }
}