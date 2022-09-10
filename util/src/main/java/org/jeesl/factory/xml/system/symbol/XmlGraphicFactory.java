package org.jeesl.factory.xml.system.symbol;

import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;
import net.sf.ahtutils.xml.symbol.Graphic;
import net.sf.exlp.factory.xml.io.XmlFileFactory;

public class XmlGraphicFactory <L extends JeeslLang,D extends JeeslDescription,
								G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
								GC extends JeeslGraphicComponent<G,GT,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlGraphicFactory.class);
	
	private Graphic q;
	
	private XmlTypeFactory<L,D,GT> xfType;
	private XmlSymbolFactory<L,D,G,GT,GS> xfSymbol;
	
	public XmlGraphicFactory(Query query){this(query.getLang(),query.getGraphic());}
	public XmlGraphicFactory(String localeCode, Graphic q)
	{
		this.q=q;
		if(q.isSetType()){xfType = new XmlTypeFactory<>(q.getType());}
		if(q.isSetSymbol()){xfSymbol = new XmlSymbolFactory<>(localeCode,q.getSymbol());}
	}
	
	public Graphic build(G graphic)
	{
		Graphic xml = build();
		
		if(q.isSetType()){xml.setType(xfType.build(graphic.getType()));}
		
		if(graphic.getType().getCode().equals(JeeslGraphicType.Code.svg.toString()))
		{
			if(q.isSetFile()){xml.setFile(XmlFileFactory.build(graphic.getData()));}
		}
		else if(graphic.getType().getCode().equals(JeeslGraphicType.Code.symbol.toString()))
		{
			if(q.isSetSymbol()){xml.setSymbol(xfSymbol.build(graphic));}
		}
		
		
		return xml;
	}
	
	public static Graphic build()
	{
		Graphic xml = new Graphic();
		return xml;
	}
}