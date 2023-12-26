package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Template;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTemplate extends AbstractXmlReportTest<Template>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTemplate.class);
	
	public TestXmlTemplate(){super(Template.class);}
	public static Template create(boolean withChildren){return (new TestXmlTemplate()).build(withChildren);} 
    
    public Template build(boolean withChildren)
    {
    	Template xml = new Template();
    	xml.setId("ahtutils-basic-template");
    	xml.setCode("myCode");
    	xml.setPosition(1);
    	xml.setVisible(true);
    	
    	if(withChildren)
    	{
    		xml.getElement().add(TestXmlElement.create(false));
        	xml.getField().add(TestXmlField.create(false));
        	
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		
    		xml.getCell().add(TestXmlCell.create(false));
    		xml.getCell().add(TestXmlCell.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTemplate test = new TestXmlTemplate();
		test.saveReferenceXml();
    }
}