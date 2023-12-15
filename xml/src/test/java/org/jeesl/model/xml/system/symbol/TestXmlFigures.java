package org.jeesl.model.xml.system.symbol;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.graphic.Figures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFigures extends AbstractXmlSymbolTest<Figures>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFigures.class);
	
	public TestXmlFigures(){super(Figures.class);}
	public static Figures create(boolean withChildren){return (new TestXmlFigures()).build(withChildren);}
    
    public Figures build(boolean withChilds)
    {
    	Figures xml = new Figures();
    	
    	if(withChilds)
    	{
    		xml.getFigure().add(TestXmlFigure.create(false));
    		xml.getFigure().add(TestXmlFigure.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlFigures test = new TestXmlFigures();
		test.saveReferenceXml();
    }
}