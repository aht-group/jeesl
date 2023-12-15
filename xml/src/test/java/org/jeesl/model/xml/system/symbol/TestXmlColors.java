package org.jeesl.model.xml.system.symbol;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.graphic.Colors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlColors extends AbstractXmlSymbolTest<Colors>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlColors.class);
	
	public TestXmlColors(){super(Colors.class);}
	public static Colors create(boolean withChildren){return (new TestXmlColors()).build(withChildren);}   
    
    public Colors build(boolean withChilds)
    {
    	Colors xml = new Colors();
    	
    	if(withChilds)
    	{
    		xml.getColor().add(TestXmlColor.create(false));
    		xml.getColor().add(TestXmlColor.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlColors test = new TestXmlColors();
		test.saveReferenceXml();
    }
}