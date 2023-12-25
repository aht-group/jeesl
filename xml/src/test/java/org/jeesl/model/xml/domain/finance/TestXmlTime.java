package org.jeesl.model.xml.domain.finance;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractAhtUtilsXmlTest;
import net.sf.ahtutils.xml.finance.Time;

public class TestXmlTime extends AbstractXmlFinanceTest<Time>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTime.class);
	
	public TestXmlTime(){super(Time.class);}
	public static Time create(boolean withChildren){return (new TestXmlTime()).build(withChildren);}
    
    public Time build(boolean withChilds)
    {
    	Time xml = new Time();
    	xml.setId(123);
    	xml.setNr(1);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	xml.setRecord(AbstractAhtUtilsXmlTest.getDefaultXmlDate());
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTime test = new TestXmlTime();
		test.saveReferenceXml();
    }
}