package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.dev.qa.Groups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlGroups extends AbstractXmlQaTest<Groups>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlGroups.class);
	
	public TestXmlGroups(){super(Groups.class);}
	public static Groups create(boolean withChildren){return (new TestXmlGroups()).build(withChildren);}   
    
    public Groups build(boolean withChilds)
    {
    	Groups xml = new Groups();
    	
    	if(withChilds)
    	{
    		xml.getGroup().add(TestXmlGroup.create(false));
    		xml.getGroup().add(TestXmlGroup.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlGroups test = new TestXmlGroups();
		test.saveReferenceXml();
    }
}