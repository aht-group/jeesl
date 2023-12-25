package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.security.TestXmlStaff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.qa.Group;

public class TestXmlGroup extends AbstractXmlQaTest<Group>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlGroup.class);
	
	public TestXmlGroup(){super(Group.class);}
	public static Group create(boolean withChildren){return (new TestXmlGroup()).build(withChildren);} 
    
    public Group build(boolean withChilds)
    {
    	Group xml = new Group();
    	xml.setId(123);
    	xml.setPosition(1);
    	xml.setName("myName");
    	
    	if(withChilds)
    	{
    		xml.setDescription(org.jeesl.model.xml.system.status.TestXmlDescription.create(false));
    		xml.getStaff().add(TestXmlStaff.create(false));xml.getStaff().add(TestXmlStaff.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlGroup test = new TestXmlGroup();
		test.saveReferenceXml();
    }
}