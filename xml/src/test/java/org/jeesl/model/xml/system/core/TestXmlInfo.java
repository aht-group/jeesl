package org.jeesl.model.xml.system.core;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.constraint.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlInfo extends AbstractXmlSystemTest<Info>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlInfo.class);
	
	public TestXmlInfo(){super(Info.class);}
	public static Info create(boolean withChildren){return (new TestXmlInfo()).build(withChildren);}
    
    public Info build(boolean withChilds)
    {
    	Info xml = new Info();

    	if(withChilds)
    	{
    		xml.getRequest().add(TestXmlRequest.create(false));xml.getRequest().add(TestXmlRequest.create(false));
    		xml.getUptime().add(TestXmlUptime.create(false));xml.getUptime().add(TestXmlUptime.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlInfo test = new TestXmlInfo();
		test.saveReferenceXml();
    }
}