package org.jeesl.model.xml.module.calendar;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCalendar extends AbstractXmlCalendarTest<Calendar>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCalendar.class);
	
	public TestXmlCalendar(){super(Calendar.class);}
	public static Calendar create(boolean withChildren){return (new TestXmlCalendar()).build(withChildren);}
    
    public Calendar build(boolean withChilds)
    {
    	Calendar xml = new Calendar();
    	xml.setId(123);

    	if(withChilds)
    	{
    		xml.setType(TestXmlType.create(false));
    		xml.getItem().add(TestXmlItem.create(false));
    		xml.getItem().add(TestXmlItem.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlCalendar test = new TestXmlCalendar();
		test.saveReferenceXml();
    }
}