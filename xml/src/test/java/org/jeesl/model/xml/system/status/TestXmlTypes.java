package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTypes extends AbstractXmlStatusTest<Types>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTypes.class);
	
	public TestXmlTypes(){super(Types.class);}
	public static Types create(boolean withChildren){return (new TestXmlTypes()).build(withChildren);}   
    
    public Types build(boolean withChilds)
    {
    	Types xml = new Types();
    	xml.setGroup("myGroup");
    	
    	if(withChilds)
    	{
    		xml.getMainType().add(TestXmlMainType.create(false));xml.getMainType().add(TestXmlMainType.create(false));
    		xml.getType().add(TestXmlType.create(false));xml.getType().add(TestXmlType.create(false));
    		xml.getSubType().add(TestXmlSubType.create(false));xml.getSubType().add(TestXmlSubType.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTypes test = new TestXmlTypes();
		test.saveReferenceXml();
    }
}