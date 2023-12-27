package org.jeesl.model.xml.system.audit;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.db.revision.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUser extends AbstractXmlAuditTest<User>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlUser.class);
	
	public TestXmlUser(){super(User.class);}
	public static User create(boolean withChildren){return (new TestXmlUser()).build(withChildren);}
    
    public User build(boolean withChilds)
    {
    	User xml = new User();
    	xml.setFirstName("myFirst");
    	xml.setLastName("myLast");
    	xml.setEmail("myEmail");
    	xml.setLabel("myLabel");
    	
    	if(withChilds)
    	{
    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlUser test = new TestXmlUser();
		test.saveReferenceXml();
    }
}