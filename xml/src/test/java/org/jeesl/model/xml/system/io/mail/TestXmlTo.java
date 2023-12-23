package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.mail.To;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTo extends AbstractXmlMailTest<To>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTo.class);
	
	public TestXmlTo(){super(To.class);}
	public static To create(boolean withChildren){return (new TestXmlTo()).build(withChildren);}
	
    public To build(boolean withChilds)
    {
    	To xml = new To();

    	if(withChilds)
    	{
    		xml.getEmailAddress().add(TestXmlEmailAddress.create(false));
    		xml.getEmailAddress().add(TestXmlEmailAddress.create(false));
    	}
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTo test = new TestXmlTo();
		test.saveReferenceXml();
    }
}