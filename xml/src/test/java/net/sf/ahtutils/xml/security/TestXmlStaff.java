package net.sf.ahtutils.xml.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;
import net.sf.ahtutils.xml.status.TestXmlDomain;
import net.sf.ahtutils.xml.status.TestXmlResponsible;
import net.sf.ahtutils.xml.status.TestXmlStatus;
import net.sf.ahtutils.xml.status.TestXmlType;

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
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
		TestXmlStaff test = new TestXmlStaff();
		test.saveReferenceXml();
    }
}