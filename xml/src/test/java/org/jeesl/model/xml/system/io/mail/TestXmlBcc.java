package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.mail.Bcc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlBcc extends AbstractXmlMailTest<Bcc>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlBcc.class);
	
	public TestXmlBcc(){super(Bcc.class);}
	public static Bcc create(boolean withChildren){return (new TestXmlBcc()).build(withChildren);}
    
    public Bcc build(boolean withChilds)
    {
    	Bcc xml = new Bcc();

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
		TestXmlBcc test = new TestXmlBcc();
		test.saveReferenceXml();
    }
}