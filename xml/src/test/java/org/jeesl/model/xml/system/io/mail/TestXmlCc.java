package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.mail.Cc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCc extends AbstractXmlMailTest<Cc>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCc.class);
	
	public TestXmlCc(){super(Cc.class);}
	public static Cc create(boolean withChildren){return (new TestXmlCc()).build(withChildren);}
	
    public Cc build(boolean withChilds)
    {
    	Cc xml = new Cc();

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
		TestXmlCc test = new TestXmlCc();
		test.saveReferenceXml();
    }
}