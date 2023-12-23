package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.mail.EmailAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlEmailAddress extends AbstractXmlMailTest<EmailAddress>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlEmailAddress.class);
	
	public TestXmlEmailAddress(){super(EmailAddress.class);}
	public static EmailAddress create(boolean withChildren){return (new TestXmlEmailAddress()).build(withChildren);}
	
    public EmailAddress build(boolean withChilds)
    {
    	EmailAddress xml = new EmailAddress();
    	xml.setName("myName");
    	xml.setEmail("my@e.mail");
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlEmailAddress test = new TestXmlEmailAddress();
		test.saveReferenceXml();
    }
}