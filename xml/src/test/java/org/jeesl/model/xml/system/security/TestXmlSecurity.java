package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSecurity extends AbstractXmlSecurityTest<Security>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSecurity.class);
	
	public TestXmlSecurity(){super(Security.class);}
	public Security create(boolean withChildren){return (new TestXmlSecurity()).build(withChildren);}
    
    public Security build(boolean withChilds)
    {
    	Security xml = new Security();
    	
    	if(withChilds)
    	{
    		xml.getCategory().add(TestXmlCategory.create(false));
    		xml.getCategory().add(TestXmlCategory.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSecurity test = new TestXmlSecurity();
		test.saveReferenceXml();
    }
}