package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Process;

public class TestXmlProcess extends AbstractXmlStatusTest<Process>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlProcess.class);
	
	public TestXmlProcess(){super(Process.class);}
	public static Process create(boolean withChildren){return (new TestXmlProcess()).build(withChildren);} 
    
    public Process build(boolean withChilds)
    {
    	Process xml = new Process();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlProcess test = new TestXmlProcess();
		test.saveReferenceXml();
    }
}