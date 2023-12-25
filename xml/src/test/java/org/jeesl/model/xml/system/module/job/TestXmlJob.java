package org.jeesl.model.xml.system.module.job;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.job.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlJob extends AbstractXmlJobTest<Job>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlJob.class);
	
	public TestXmlJob(){super(Job.class);}
	public static Job create(boolean withChildren){return (new TestXmlJob()).build(withChildren);}
    
    public Job build(boolean withChilds)
    {
    	Job xml = new Job();
    	xml.setId(123);
    	
    	if(withChilds)
    	{
    		xml.setTemplate(TestXmlTemplate.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlJob test = new TestXmlJob();
		test.saveReferenceXml();
    }
}