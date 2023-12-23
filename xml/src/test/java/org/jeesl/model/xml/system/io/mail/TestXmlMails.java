package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlMails extends AbstractXmlMailTest<Mails>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlMails.class);
	
	public TestXmlMails(){super(Mails.class);}
	public static Mails create(boolean withChildren){return (new TestXmlMails()).build(withChildren);}
	
    public Mails build(boolean withChilds)
    {
    	Mails xml = new Mails();
    	xml.setDir("myDir");
    	xml.setQueue(5);
    	
    	if(withChilds)
    	{
    		xml.getMail().add(TestXmlMail.create(false));
    	}
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlMails test = new TestXmlMails();
		test.saveReferenceXml();
    }
}