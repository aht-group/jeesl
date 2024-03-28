package org.jeesl.model.xml.io.cms.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFinding extends AbstractXmlTextOldTest<Finding>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFinding.class);
	
	public TestXmlFinding(){super(Finding.class);}
	public static Finding create(boolean withChildren){return (new TestXmlFinding()).build(withChildren);}
    
    public Finding build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Finding create(boolean withChilds, String key, String description)
    {
    		Finding xml = new Finding();
	    	xml.setVersion(1);
	    	xml.setKey(key);
	    	xml.setValue(description);
	    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlFinding test = new TestXmlFinding();
		test.saveReferenceXml();
    }
}