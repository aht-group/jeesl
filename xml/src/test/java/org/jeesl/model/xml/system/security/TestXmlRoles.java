package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRoles extends AbstractXmlSecurityTest<Roles>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRoles.class);
	
	public TestXmlRoles(){super(Roles.class);}
	public static Roles create(boolean withChildren){return (new TestXmlRoles()).build(withChildren);}
    
    public Roles build(boolean withChilds)
    {
    	Roles xml = new Roles();
    	xml.setType("myType");
    	
    	if(withChilds)
    	{
    		xml.getRoles().add(TestXmlRoles.create(false));xml.getRoles().add(TestXmlRoles.create(false));
    		xml.getRole().add(TestXmlRole.create(false));xml.getRole().add(TestXmlRole.create(false));
    		
    	}
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlRoles test = new TestXmlRoles();
		test.saveReferenceXml();
    }
}