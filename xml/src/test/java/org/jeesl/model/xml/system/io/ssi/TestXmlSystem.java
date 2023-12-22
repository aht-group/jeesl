package org.jeesl.model.xml.system.io.ssi;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.ssi.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSystem extends AbstractXmlSsiTest<System>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSystem.class);
	
	public TestXmlSystem(){super(System.class);}
	public static System create(boolean withChildren){return (new TestXmlSystem()).build(withChildren);} 
       
    public System build(boolean withChilds)
    {
    	System xml = new System();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setName("myName");
    	
    	if(withChilds)
    	{
    		
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSystem test = new TestXmlSystem();
		test.saveReferenceXml();
    }
}