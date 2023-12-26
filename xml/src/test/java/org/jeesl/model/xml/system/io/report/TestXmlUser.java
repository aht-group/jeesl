package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUser extends AbstractXmlReportTest<User>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlUser.class);
	
	public TestXmlUser(){super(User.class);}
	public static User create(boolean withChildren){return (new TestXmlUser()).build(withChildren);} 
    
    public User build(boolean withChildren)
    {
    	User xml = new User();
    	xml.setValue("myUser");
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlUser test = new TestXmlUser();
		test.saveReferenceXml();
    }
}