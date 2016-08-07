package org.jeesl.model.xml.text;

import org.jeesl.UtilsXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.text.Impact;

public class TestXmlImpact extends AbstractXmlTextTest<Impact>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlImpact.class);
	
	public TestXmlImpact(){super(Impact.class);}
	public static Impact create(boolean withChildren){return (new TestXmlImpact()).build(withChildren);}
    
    public Impact build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Impact create(boolean withChilds, String key, String description)
    {
    	Impact xml = new Impact();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setValue(description);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		UtilsXmlTestBootstrap.init();
		TestXmlImpact test = new TestXmlImpact();
		test.saveReferenceXml();
    }
}