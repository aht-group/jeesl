package org.jeesl.model.xml.module.ts;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlData extends AbstractXmlTimeseriesTest<Data>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlData.class);
	
	public TestXmlData(){super(Data.class);}
	public static Data create(boolean withChildren){return (new TestXmlData()).build(withChildren);}
    
    public Data build(boolean withChilds)
    {
    	Data xml = new Data();
    	xml.setId(123);
    	xml.setValue(234.56);
    	xml.setRecord(AbstractXmlTimeseriesTest.getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlData test = new TestXmlData();
		test.saveReferenceXml();
    }
}