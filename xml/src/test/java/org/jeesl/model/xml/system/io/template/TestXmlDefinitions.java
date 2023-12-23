package org.jeesl.model.xml.system.io.template;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.template.Definitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlDefinitions extends AbstractXmlTemplateTest<Definitions>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlDefinitions.class);
	
	public TestXmlDefinitions(){super(Definitions.class);}
	public static Definitions create(boolean withChildren){return (new TestXmlDefinitions()).build(withChildren);} 
    
    public Definitions build(boolean withChilds)
    {
    	Definitions xml = new Definitions();
    	
    	if(withChilds)
    	{
    		xml.getDefinition().add(TestXmlDefinition.create(false));
    		xml.getDefinition().add(TestXmlDefinition.create(false));
    	}	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlDefinitions test = new TestXmlDefinitions();
		test.saveReferenceXml();
    }
}