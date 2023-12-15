package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlOrder extends AbstractXmlStatusTest<Order>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlOrder.class);
	
	public TestXmlOrder(){super(Order.class);}
	public static Order create(boolean withChildren){return (new TestXmlOrder()).build(withChildren);} 
    
    public Order build(boolean withChilds)
    {
    	Order xml = new Order();
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
		TestXmlOrder test = new TestXmlOrder();
		test.saveReferenceXml();
    }
}