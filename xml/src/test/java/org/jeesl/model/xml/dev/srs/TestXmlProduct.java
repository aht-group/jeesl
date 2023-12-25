package org.jeesl.model.xml.dev.srs;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlProduct extends AbstractXmlSrsTest<Product>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlProduct.class);
	
	public TestXmlProduct(){super(Product.class);}
	public static Product create(boolean withChildren){return (new TestXmlProduct()).build(withChildren);}
    
    public Product build(boolean withChildren)
    {
    	Product xml = new Product();
    
    	xml.setCode("JEESL");
    	xml.setName("JavaEE Support Libraries");
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlProduct test = new TestXmlProduct();
		test.saveReferenceXml();
    }
}