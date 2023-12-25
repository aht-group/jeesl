package org.jeesl.model.xml.module.calendar;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlItem extends AbstractXmlCalendarTest<Item>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlItem.class);
	
	public TestXmlItem(){super(Item.class);}
	public static Item create(boolean withChildren){return (new TestXmlItem()).build(withChildren);}
    
    public Item build(boolean withChilds)
    {
    	Item xml = new Item();
    	xml.setId(123);
    	xml.setAllDay(true);
    	xml.setStart(AbstractXmlCalendarTest.getDefaultXmlDate());
    	xml.setEnd(AbstractXmlCalendarTest.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		xml.setType(TestXmlType.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlItem test = new TestXmlItem();
		test.saveReferenceXml();
    }
}