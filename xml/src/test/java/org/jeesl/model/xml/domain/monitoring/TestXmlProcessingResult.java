package org.jeesl.model.xml.domain.monitoring;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.ProcessingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlProcessingResult extends AbstractXmlMonitoringTest<ProcessingResult>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlProcessingResult.class);
	
	public TestXmlProcessingResult(){super(ProcessingResult.class);}
	public static ProcessingResult create(boolean withChildren){return (new TestXmlProcessingResult()).build(withChildren);}
    
    public ProcessingResult build(boolean withChilds)
    {
    	ProcessingResult xml = new ProcessingResult();
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	if(withChilds){}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlProcessingResult test = new TestXmlProcessingResult();
		test.saveReferenceXml();
    }
}