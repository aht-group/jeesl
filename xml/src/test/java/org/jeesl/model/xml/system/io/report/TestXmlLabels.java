package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Labels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlLabels extends AbstractXmlReportTest<Labels>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlLabels.class);
	
	public TestXmlLabels(){super(Labels.class);}
	public static Labels create(boolean withChildren){return (new TestXmlLabels()).build(withChildren);}    
    
    public Labels build(boolean withChildren)
    {
    	Labels xml = new Labels();
   
    	if(withChildren)
    	{
    		xml.getLabel().add(TestXmlLabel.create(false));
    	}

    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlLabels test = new TestXmlLabels();
		test.saveReferenceXml();
    }
}