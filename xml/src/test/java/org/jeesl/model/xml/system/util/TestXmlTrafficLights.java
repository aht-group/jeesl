package org.jeesl.model.xml.system.util;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTrafficLights extends AbstractXmlUtilsTest<TrafficLights>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTrafficLights.class);
	
	public TestXmlTrafficLights(){super(TrafficLights.class);}
	public static TrafficLights create(boolean withChildren){return (new TestXmlTrafficLights()).build(withChildren);}
    
    public TrafficLights build(boolean withChilds)
    {
    	TrafficLights xml = new TrafficLights();
    	
    	if(withChilds)
    	{
    		xml.getTrafficLight().add(TestXmlTrafficLight.create(false));
    		xml.getTrafficLight().add(TestXmlTrafficLight.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTrafficLights test = new TestXmlTrafficLights();
		test.saveReferenceXml();
    }
}