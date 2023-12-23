package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlStatement;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTest extends AbstractXmlQaTest<org.jeesl.model.xml.module.dev.qa.Test>
{
	final static Logger logger = LoggerFactory.getLogger(org.jeesl.model.xml.module.dev.qa.Test.class);
	
	public TestXmlTest(){super(org.jeesl.model.xml.module.dev.qa.Test.class);}
	public static org.jeesl.model.xml.module.dev.qa.Test create(boolean withChildren){return (new TestXmlTest()).build(withChildren);}   
    
    public org.jeesl.model.xml.module.dev.qa.Test build(boolean withChilds)
    {
    	org.jeesl.model.xml.module.dev.qa.Test xml = new org.jeesl.model.xml.module.dev.qa.Test();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setName("myName");
    	xml.setDuration(120);
    	
    	if(withChilds)
    	{
    		xml.setStatus(TestXmlStatus.create(false));
    		xml.setStatement(TestXmlStatement.create(false));
    		
    		xml.setReference(TestXmlReference.create(false));
    		xml.setDescription(TestXmlDescription.create(false));
    		xml.setPreCondition(TestXmlPreCondition.create(false));
    		xml.setSteps(TestXmlSteps.create(false));
    		xml.setExpected(TestXmlExpected.create(false));
    		
    		xml.setResults(TestXmlResults.create(false));
    		xml.setInfo(TestXmlInfo.create(false));
    		xml.setGroups(TestXmlGroups.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlTest test = new TestXmlTest();
		test.saveReferenceXml();
    }
}