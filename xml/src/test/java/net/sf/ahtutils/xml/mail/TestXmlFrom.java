package net.sf.ahtutils.xml.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;

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
		UtilsXmlTestBootstrap.init();
		TestXmlFrom test = new TestXmlFrom();
		test.saveReferenceXml();
    }
}