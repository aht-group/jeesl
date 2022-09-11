package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUser extends AbstractXmlSecurityTest<User>
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

    	if(withChilds)
    	{
    		xml.setStaffs(TestXmlStaffs.create(false));
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