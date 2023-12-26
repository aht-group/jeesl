package org.jeesl.model.xml.domain.monitoring;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.Indicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlIndicator extends AbstractXmlMonitoringTest<Indicator>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlIndicator.class);
	
	public TestXmlIndicator(){super(Indicator.class);}
	public static Indicator create(boolean withChildren){return (new TestXmlIndicator()).build(withChildren);}
    
    public Indicator build(boolean withChilds)
    {
    	Indicator xml = new Indicator();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	
    	if(withChilds)
    	{
    		xml.getDataSet().add(TestXmlDataSet.create(false));xml.getDataSet().add(TestXmlDataSet.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlIndicator test = new TestXmlIndicator();
		test.saveReferenceXml();
    }
}