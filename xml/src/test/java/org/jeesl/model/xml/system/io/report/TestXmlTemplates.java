package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTemplates extends AbstractXmlReportTest<Templates>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTemplates.class);
	
	public TestXmlTemplates(){super(Templates.class);}
	public static Templates create(boolean withChildren){return (new TestXmlTemplates()).build(withChildren);} 
    
    public Templates build(boolean withChildren)
    {
    	Templates templates = new Templates();
    	
    	if(withChildren)
    	{
    		templates.getTemplate().add(TestXmlTemplate.create(false));
    		templates.getTemplate().add(TestXmlTemplate.create(false));
    	}
    	
    	return templates;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTemplates test = new TestXmlTemplates();
		test.saveReferenceXml();
    }
}