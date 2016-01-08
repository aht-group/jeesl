package net.sf.ahtutils.xml.issue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;

public class TestXmlTask extends AbstractXmlIssueTest<Task>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTask.class);
	
	public TestXmlTask(){super(Task.class);}
	public static Task create(boolean withChildren){return (new TestXmlTask()).build(withChildren);} 
    
    public Task build(boolean withChilds)
    {
    	Task xml = new Task();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setName("myName");
    	
    	if(withChilds)
    	{
    		xml.setTasks(TestXmlTasks.create(false));
    	}
    	    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();	
		TestXmlTask test = new TestXmlTask();
		test.saveReferenceXml();
    }
}