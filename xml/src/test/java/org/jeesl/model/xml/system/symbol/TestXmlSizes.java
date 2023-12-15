package org.jeesl.model.xml.system.symbol;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.graphic.Sizes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSizes extends AbstractXmlSymbolTest<Sizes>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSizes.class);
	
	public TestXmlSizes(){super(Sizes.class);}
	public static Sizes create(boolean withChildren){return (new TestXmlSizes()).build(withChildren);}   
    
    public Sizes build(boolean withChilds)
    {
    	Sizes xml = new Sizes();
    	
    	if(withChilds)
    	{
    		xml.getSize().add(TestXmlSize.create(false));
    		xml.getSize().add(TestXmlSize.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSizes test = new TestXmlSizes();
		test.saveReferenceXml();
    }
}