package org.jeesl.model.xml.system.io.mail;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.mail.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlLink extends AbstractXmlMailTest<Link>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlLink.class);
	
	public TestXmlLink(){super(Link.class);}
	public static Link create(boolean withChildren){return (new TestXmlLink()).build(withChildren);}
    
    public Link build(boolean withChilds)
    {
    	Link xml = new Link();

    	xml.setId(123);
    	xml.setRefId(345);
    	xml.setType("myType");
    	xml.setCode("myCode");
    	xml.setUrl("myUrl");
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlLink test = new TestXmlLink();
		test.saveReferenceXml();
    }
}