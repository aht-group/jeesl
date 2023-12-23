package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.mail.From;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFrom extends AbstractXmlMailTest<From>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFrom.class);
	
	public TestXmlFrom(){super(From.class);}
	public static From create(boolean withChildren){return (new TestXmlFrom()).build(withChildren);}
	
    public From build(boolean withChilds)
    {
    	From xml = new From();

    	if(withChilds)
    	{
    		xml.setEmailAddress(TestXmlEmailAddress.create(false));
    	}
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlFrom test = new TestXmlFrom();
		test.saveReferenceXml();
    }
}