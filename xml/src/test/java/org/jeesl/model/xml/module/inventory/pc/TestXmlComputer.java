package org.jeesl.model.xml.module.inventory.pc;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlComputer extends AbstractXmlInventoryPcTest<Computer>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlComputer.class);
	
	public TestXmlComputer(){super(Computer.class);}
	public static Computer create(boolean withChildren){return (new TestXmlComputer()).build(withChildren);}
    
    public Computer build(boolean withChilds)
    {
    	Computer xml = new Computer();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setName("myName");
    	xml.setUuid("myUuid");
    	    	
    	if(withChilds)
    	{
    		xml.setHardware(TestXmlHardware.create(false));
        	xml.setSoftware(TestXmlSoftware.create(false));
    	}
    	
    	return xml;
    }
   
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlComputer test = new TestXmlComputer();
		test.saveReferenceXml();
    }
}