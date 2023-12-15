package org.jeesl.model.xml.system.symbol;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.graphic.Figure;
import org.jeesl.model.xml.io.locale.status.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFigure extends AbstractXmlSymbolTest<Figure>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFigure.class);
	
	public TestXmlFigure(){super(Figure.class);}
	public static Figure create(boolean withChildren){return (new TestXmlFigure()).build(withChildren);}
    
    public Figure build(boolean withChilds)
    {
    	Figure xml = new Figure();
    	xml.setId(123l);
    	xml.setStyle(new Style());
    	xml.setCss(true);
    	xml.setSize(2);
    	xml.setColor("abcfef");
    	xml.setRotation(3);
    	xml.setOffsetX(2);
    	xml.setOffsetY(4);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlFigure test = new TestXmlFigure();
		test.saveReferenceXml();
    }
}