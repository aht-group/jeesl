package org.jeesl.model.xml.system.core;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.constraint.Release;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRelease extends AbstractXmlSystemTest<Release>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRelease.class);
	
	public TestXmlRelease(){super(Release.class);}
	public static Release create(boolean withChildren){return (new TestXmlRelease()).build(withChildren);}
        
    public Release build(boolean withChilds)
    {
    	Release xml = new Release();
    	xml.setVersion("0.1.1");
    	xml.setDate(AbstractXmlSystemTest.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		xml.setDescriptions(TestXmlDescriptions.create(false));
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