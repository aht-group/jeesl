package org.jeesl.model.xml.dev.srs;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRelease extends AbstractXmlSrsTest<Release>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRelease.class);
	
	public TestXmlRelease(){super(Release.class);}
	public static Release create(boolean withChildren){return (new TestXmlRelease()).build(withChildren);}
    
    public Release build(boolean withChildren)
    {
    	Release xml = new Release();
    	xml.setCode("myCode");
    	xml.setName("myName");
    	if(withChildren)
    	{
    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlRelease test = new TestXmlRelease();
		test.saveReferenceXml();
    }
}