package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.system.util.text.TestXmlExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlMail extends AbstractXmlMailTest<Mail>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlMail.class);
	
	public TestXmlMail(){super(Mail.class);}
	public static Mail create(boolean withChildren){return (new TestXmlMail()).build(withChildren);}
	
    public Mail build(boolean withChilds)
    {
    	Mail xml = new Mail();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setLang("myLang");
    	xml.setType("myType");
    	xml.setMsgId("myMsgID");
    	xml.setDir("myDir");
    	xml.setExample(TestXmlExample.create(false));
    	xml.setTest(true);
    	
    	if(withChilds)
    	{
    		xml.setMail(TestXmlMail.create(false));
    		xml.setHeader(TestXmlHeader.create(false));
    		xml.getTemplate().add(TestXmlTemplate.create(false));
    		xml.getTemplate().add(TestXmlTemplate.create(false));
    		xml.getAttachment().add(TestXmlAttachment.create(false));xml.getAttachment().add(TestXmlAttachment.create(false));
    		xml.getImage().add(TestXmlImage.create(false));xml.getImage().add(TestXmlImage.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlMail test = new TestXmlMail();
		test.saveReferenceXml();
    }
}