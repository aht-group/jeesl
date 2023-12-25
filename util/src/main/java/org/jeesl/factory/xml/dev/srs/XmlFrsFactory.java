package org.jeesl.factory.xml.dev.srs;

import org.jeesl.model.xml.dev.srs.Fr;
import org.jeesl.model.xml.dev.srs.Frs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFrsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlFrsFactory.class);
	
    public static Frs build(){return new Frs();}
    
    public static Frs build( Fr fr) {Frs xml = build(); xml.getFr().add(fr); return xml;}
   
    
}