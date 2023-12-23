package org.jeesl.model.xml.system.io.template;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.template.Definition;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlDefinition extends AbstractXmlTemplateTest<Definition>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlDefinition.class);
	
	public TestXmlDefinition(){super(Definition.class);}
	public static Definition create(boolean withChildren){return (new TestXmlDefinition()).build(withChildren);} 
    
    public Definition build(boolean withChilds)
    {
    	Definition xml = new Definition();
    	xml.setId(123);
    	xml.setPosition(2);
    	xml.setVisible(true);
    	
    	if(withChilds)
    	{
    		xml.setType(TestXmlType.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlDefinition test = new TestXmlDefinition();
		test.saveReferenceXml();
    }
}