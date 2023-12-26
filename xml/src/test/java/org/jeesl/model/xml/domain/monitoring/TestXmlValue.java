package org.jeesl.model.xml.domain.monitoring;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlValue extends AbstractXmlMonitoringTest<Value>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlValue.class);
	
	public TestXmlValue(){super(Value.class);}
	public static Value create(boolean withChildren){return (new TestXmlValue()).build(withChildren);}
    
    public Value build(boolean withChildren)
    {
    	Value xml = new Value();
    	xml.setValue("myValue");
    	xml.setType("myType");
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlValue test = new TestXmlValue();
		test.saveReferenceXml();
    }
}