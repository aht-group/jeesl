package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlAttachment extends AbstractXmlMailTest<Attachment>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlAttachment.class);
	
	public TestXmlAttachment(){super(Attachment.class);}
	public static Attachment create(boolean withChildren){return (new TestXmlAttachment()).build(withChildren);}
	
    public Attachment build(boolean withChilds)
    {
    	Attachment xml = new Attachment();
    	
    	if(withChilds)
    	{
    		xml.setData("myBinaryData".getBytes());
    		xml.setFile(new org.exlp.model.xml.io.File());
    	}
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlAttachment test = new TestXmlAttachment();
		test.saveReferenceXml();
    }
}