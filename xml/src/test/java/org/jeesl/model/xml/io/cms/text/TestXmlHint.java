package org.jeesl.model.xml.io.cms.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.cms.chars.Hint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlHint extends AbstractXmlTextOldTest<Hint>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlHint.class);
	
	public TestXmlHint(){super(Hint.class);}
	public static Hint create(boolean withChildren){return (new TestXmlHint()).build(withChildren);}
    
    public Hint build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Hint create(boolean withChilds, String key, String description)
    {
    	Hint xml = new Hint();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setValue(description);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlHint test = new TestXmlHint();
		test.saveReferenceXml();
    }
}