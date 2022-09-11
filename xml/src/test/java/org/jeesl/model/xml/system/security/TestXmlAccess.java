package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlAccess extends AbstractXmlSecurityTest<Access>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlAccess.class);
	
	public TestXmlAccess(){super(Access.class);}
	public static Access create(boolean withChildren){return (new TestXmlAccess()).build(withChildren);}
    
    public Access build(boolean withChilds)
    {
    	Access xml = new Access();
    	xml.setPublicUser(true);
    	xml.setAuthenticatedUser(true);
    	return xml;
    }

	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlAccess test = new TestXmlAccess();
		test.saveReferenceXml();
    }
}