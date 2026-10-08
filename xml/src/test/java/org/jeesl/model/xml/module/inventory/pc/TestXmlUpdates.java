package org.jeesl.model.xml.module.inventory.pc;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUpdates extends AbstractXmlInventoryPcTest<Updates>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlUpdates.class);
	
	public TestXmlUpdates(){super(Updates.class);}
	public static Updates create(boolean withChildren){return (new TestXmlUpdates()).build(withChildren);}
    
    public Updates build(boolean withChilds)
    {
    	Updates xml = new Updates();
    	
    	if(withChilds)
    	{
    		xml.getUpdate().add(TestXmlUpdate.create(false));
    		xml.getUpdate().add(TestXmlUpdate.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlUpdates test = new TestXmlUpdates();
		test.saveReferenceXml();
    }
}