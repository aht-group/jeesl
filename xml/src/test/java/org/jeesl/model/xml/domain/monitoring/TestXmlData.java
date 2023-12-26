package org.jeesl.model.xml.domain.monitoring;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractAhtUtilsXmlTest;

public class TestXmlData extends AbstractXmlMonitoringTest<Data>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlData.class);
	
	public TestXmlData(){super(Data.class);}
	public static Data create(boolean withChildren){return (new TestXmlData()).build(withChildren);}
    
    public Data build(boolean withChilds)
    {
    	Data xml = new Data();
    	xml.setId(123);
    	xml.setRange(1000);
    	xml.setRecord(AbstractAhtUtilsXmlTest.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		xml.setIndicator(TestXmlIndicator.create(false));
    		xml.setObserver(TestXmlObserver.create(false));
    		xml.getValue().add(TestXmlValue.create(false));xml.getValue().add(TestXmlValue.create(false));
    	}
    	
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlData test = new TestXmlData();
		test.saveReferenceXml();
    }
}