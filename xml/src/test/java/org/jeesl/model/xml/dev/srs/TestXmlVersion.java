package org.jeesl.model.xml.dev.srs;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.dev.srs.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlVersion extends AbstractXmlSrsTest<Version>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlVersion.class);
	
	public TestXmlVersion(){super(Version.class);}
	public static Version create(boolean withChildren){return (new TestXmlVersion()).build(withChildren);}
    
    public Version build(boolean withChildren)
    {
    	Version xml = new Version();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	
    	if(withChildren)
    	{
    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlVersion test = new TestXmlVersion();
		test.saveReferenceXml();
    }
}