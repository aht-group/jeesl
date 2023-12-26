package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Resource;
import org.jeesl.model.xml.io.report.Resource.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlResource extends AbstractXmlReportTest<Resource>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlResource.class);
	
	public TestXmlResource(){super(Resource.class);}
	public static Resource create(boolean withChildren){return (new TestXmlResource()).build(withChildren);}
    
    public Resource build(boolean withChildren)
    {
    	Resource resource = new Resource();
    	resource.setName("logo");
    	resource.setType("image");
    	Value myValue = new Resource.Value();
    	myValue.setValue("logo.png");
    	resource.setValue(myValue);
    	return resource;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlResource test = new TestXmlResource();
		test.saveReferenceXml();
    }
}