package org.jeesl.model.xml.system.module.job;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.job.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTemplate extends AbstractXmlJobTest<Template>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTemplate.class);
	
	public TestXmlTemplate(){super(Template.class);}
	public static Template create(boolean withChildren){return (new TestXmlTemplate()).build(withChildren);}
    
    public Template build(boolean withChilds)
    {
    	Template xml = new Template();
    	xml.setId(123);
    	xml.setCode("myCode");
    	
    	if(withChilds)
    	{
    		
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