package org.jeesl.model.xml.system.symbol;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.graphic.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlColor extends AbstractXmlSymbolTest<Color>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlColor.class);
	
	public TestXmlColor(){super(Color.class);}
	public static Color create(boolean withChildren){return (new TestXmlColor()).build(withChildren);}
    
    public Color build(boolean withChilds){return create(withChilds,"myGroup","myValue");}
    public static Color create(boolean withChilds, String group, String value)
    {
    	Color xml = new Color();
    	xml.setGroup(group);
    	xml.setValue(value);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlColor test = new TestXmlColor();
		test.saveReferenceXml();
    }
}