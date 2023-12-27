package org.jeesl.model.xml.module.inventory.pc;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSoftware extends AbstractXmlInventoryPcTest<Software>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlComputer.class);
	
	public TestXmlSoftware(){super(Software.class);}
	public static Software create(boolean withChildren){return (new TestXmlSoftware()).build(withChildren);}
    
    public Software build(boolean withChilds)
    {
    	Software xml = new Software();
    	xml.setId(123l);

    	if(withChilds)
    	{
        	xml.setUpdates(TestXmlUpdates.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSoftware test = new TestXmlSoftware();
		test.saveReferenceXml();
    }
}