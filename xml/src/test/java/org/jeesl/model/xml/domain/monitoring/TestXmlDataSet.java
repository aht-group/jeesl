package org.jeesl.model.xml.domain.monitoring;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlDataSet extends AbstractXmlMonitoringTest<DataSet>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlDataSet.class);
	
	public TestXmlDataSet(){super(DataSet.class);}
	public static DataSet create(boolean withChildren){return (new TestXmlDataSet()).build(withChildren);}
    
    public DataSet build(boolean withChilds)
    {
    	DataSet xml = new DataSet();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	if(withChilds)
    	{
    		xml.setIndicator(TestXmlIndicator.create(false));
    		xml.setObserver(TestXmlObserver.create(false));
    		xml.getData().add(TestXmlData.create(false));xml.getData().add(TestXmlData.create(false));
    	}
    	return xml;
    }

	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlDataSet test = new TestXmlDataSet();
		test.saveReferenceXml();
    }
}