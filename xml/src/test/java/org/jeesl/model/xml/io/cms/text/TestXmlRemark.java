package org.jeesl.model.xml.io.cms.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRemark extends AbstractXmlTextOldTest<Remark>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRemark.class);
	
	public TestXmlRemark(){super(Remark.class);}
	public static Remark create(boolean withChildren){return (new TestXmlRemark()).build(withChildren);}
    
    public Remark build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Remark create(boolean withChilds, String key, String description)
    {
    	Remark xml = new Remark();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setValue(description);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlRemark test = new TestXmlRemark();
		test.saveReferenceXml();
    }
}