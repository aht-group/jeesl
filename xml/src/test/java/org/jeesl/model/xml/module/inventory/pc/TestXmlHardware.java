package org.jeesl.model.xml.module.inventory.pc;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlHardware extends AbstractXmlInventoryPcTest<Hardware>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlHardware.class);
	
	public TestXmlHardware(){super(Hardware.class);}
	public static Hardware create(boolean withChildren){return (new TestXmlHardware()).build(withChildren);}
    
    public Hardware build(boolean withChilds)
    {
    	Hardware xml = new Hardware();
    	xml.setId(123l);
    	xml.setManufacturer("Lasse");
    	xml.setModel("XYZ");
    	xml.setSerial("01234-56789");    
    	
    	if(withChilds)
    	{
    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlHardware test = new TestXmlHardware();
		test.saveReferenceXml();
    }
}