package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRule extends AbstractXmlSecurityTest<Rule>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRule.class);
	
	public TestXmlRule(){super(Rule.class);}
	public static Rule create(boolean withChildren){return (new TestXmlRule()).build(withChildren);}
    
    public Rule build(boolean withChilds)
    {
    	Rule xml = new Rule();
    	
    	xml.setType("myType");
    	xml.setCode("myCode");
    	xml.setValid(true);
    	
    	xml.setMin(1);
    	xml.setMax(8);
    	xml.setActual(4);
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlRule test = new TestXmlRule();
		test.saveReferenceXml();
    }
}