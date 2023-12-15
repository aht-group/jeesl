package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlDataType extends AbstractXmlStatusTest<DataType>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlDataType.class);
	
	public TestXmlDataType(){super(DataType.class);}
	public static DataType create(boolean withChildren){return (new TestXmlDataType()).build(withChildren);} 
    
    public DataType build(boolean withChilds)
    {
    	DataType xml = new DataType();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlDataType test = new TestXmlDataType();
		test.saveReferenceXml();
    }
}