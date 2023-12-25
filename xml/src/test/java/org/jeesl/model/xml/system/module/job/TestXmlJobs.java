package org.jeesl.model.xml.system.module.job;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.job.Jobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlJobs extends AbstractXmlJobTest<Jobs>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlJobs.class);
	
	public TestXmlJobs(){super(Jobs.class);}
	public static Jobs create(boolean withChildren){return (new TestXmlJobs()).build(withChildren);}
    
    public Jobs build(boolean withChilds)
    {
    	Jobs xml = new Jobs();
    	xml.setQueue(4);
    	
    	if(withChilds)
    	{
    		xml.getJob().add(TestXmlJob.create(false));
    		xml.getJob().add(TestXmlJob.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlJobs test = new TestXmlJobs();
		test.saveReferenceXml();
    }
}