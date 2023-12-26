package org.jeesl.model.xml.domain.monitoring;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlObserver extends AbstractXmlMonitoringTest<Observer>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlObserver.class);
	
	public TestXmlObserver(){super(Observer.class);}
	public static Observer create(boolean withChildren){return (new TestXmlObserver()).build(withChildren);}
    
    public Observer build(boolean withChilds)
    {
    	Observer xml = new Observer();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	if(withChilds){}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlObserver test = new TestXmlObserver();
		test.saveReferenceXml();
    }
}