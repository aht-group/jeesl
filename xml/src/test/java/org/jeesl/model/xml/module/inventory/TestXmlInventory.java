package org.jeesl.model.xml.module.inventory;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.inventory.pc.TestXmlComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlInventory extends AbstractXmlInventoryTest<Inventory>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlInventory.class);
	
	public TestXmlInventory(){super(Inventory.class);}
	public static Inventory create(boolean withChildren){return (new TestXmlInventory()).build(withChildren);}
    
    public Inventory build(boolean withChilds)
    {
    	Inventory xml = new Inventory();
    	
    	if(withChilds)
    	{
    		xml.getComputer().add(TestXmlComputer.create(false));    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlInventory test = new TestXmlInventory();
		test.saveReferenceXml();
    }
} 