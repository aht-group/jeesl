package org.jeesl.model.xml.dev.srs;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlMeta extends AbstractXmlSrsTest<Meta>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlMeta.class);
	
	public TestXmlMeta(){super(Meta.class);}
	public static Meta create(boolean withChildren){return (new TestXmlMeta()).build(withChildren);}
    
    public Meta build(boolean withChildren)
    {
    	Meta xml = new Meta();
    
    	if(withChildren)
    	{
    		xml.setProduct(TestXmlProduct.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlMeta test = new TestXmlMeta();
		test.saveReferenceXml();
    }
}