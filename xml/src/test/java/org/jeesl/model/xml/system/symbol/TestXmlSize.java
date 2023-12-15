package org.jeesl.model.xml.system.symbol;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.graphic.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSize extends AbstractXmlSymbolTest<Size>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSize.class);
	
	public TestXmlSize(){super(Size.class);}
	public static Size create(boolean withChildren){return (new TestXmlSize()).build(withChildren);}
    
    public Size build(boolean withChilds){return create(withChilds,"myGroup",12);}
    public static Size create(boolean withChilds, String group, int value)
    {
    	Size xml = new Size();
    	xml.setGroup(group);
    	xml.setValue(value);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSize test = new TestXmlSize();
		test.saveReferenceXml();
    }
}