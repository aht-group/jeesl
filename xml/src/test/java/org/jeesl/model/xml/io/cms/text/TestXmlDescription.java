package org.jeesl.model.xml.io.cms.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.cms.text.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlDescription extends AbstractXmlTextOldTest<Description>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlDescription.class);
	
	public TestXmlDescription(){super(Description.class);}
	public static Description create(boolean withChildren){return (new TestXmlDescription()).build(withChildren);}
    
    public Description build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Description create(boolean withChilds, String key, String description)
    {
    	Description xml = new Description();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setValue(description);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlDescription test = new TestXmlDescription();
		test.saveReferenceXml();
    }
}