package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlDomain;
import org.jeesl.model.xml.system.status.TestXmlLevel;
import org.jeesl.model.xml.system.status.TestXmlResponsible;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlStaff extends AbstractXmlSecurityTest<Staff>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlStaff.class);
	
	public TestXmlStaff(){super(Staff.class);}
	public static Staff create(boolean withChildren){return (new TestXmlStaff()).build(withChildren);}
    
    public Staff build(boolean withChilds)
    {
    	Staff xml = new Staff();
    	xml.setId(123);
    	
    	if(withChilds)
    	{
    		xml.setRole(TestXmlRole.create(false));
    		xml.setUser(TestXmlUser.create(false));
    		xml.setDomain(TestXmlDomain.create(false));
    		
    		xml.setStatus(TestXmlStatus.create(false));
    		xml.setType(TestXmlType.create(false));
    		xml.setResponsible(TestXmlResponsible.create(false));
    		xml.setLevel(TestXmlLevel.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlStaff test = new TestXmlStaff();
		test.saveReferenceXml();
    }
}