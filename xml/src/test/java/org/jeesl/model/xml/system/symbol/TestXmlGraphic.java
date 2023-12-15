package org.jeesl.model.xml.system.symbol;

import org.exlp.model.xml.io.File;
import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.graphic.Graphic;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlGraphic extends AbstractXmlSymbolTest<Graphic>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlGraphic.class);
    
	public TestXmlGraphic(){super(Graphic.class);}
	public static Graphic create(boolean withChildren){return (new TestXmlGraphic()).build(withChildren);}
    
    public Graphic build(boolean withChilds)
    {
    	Graphic xml = new Graphic();
    	xml.setId(123l);
    	
    	if(withChilds)
    	{
    		xml.setFile(new File());
    		xml.setType(TestXmlType.create(false));
    		xml.setSymbol(TestXmlSymbol.create(false));
    		xml.setFigures(TestXmlFigures.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlGraphic test = new TestXmlGraphic();
		test.saveReferenceXml();
    }
}