package org.jeesl.factory.xml.system.symbol;

import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.symbol.Color;
import net.sf.ahtutils.xml.symbol.Colors;

public class XmlColorsFactory <G extends JeeslGraphic<?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlColorsFactory.class);
		
	private Colors q;
	
	public XmlColorsFactory(Colors q)
	{
		this.q=q;
		
	}
	
	public Colors build(G graphic)
	{
		Colors xml = build();
		
		if(q.isSetColor())
		{
			xml.getColor().add(XmlColorFactory.build(JeeslGraphicShape.Color.outer, graphic.getColor()));
		}
		
		return xml;
	}
	
	public static Colors build()
	{
		Colors xml = new Colors();
		return xml;
	}
	
	public static Colors build(Color color)
	{
		Colors xml = build();
		xml.getColor().add(color);
		return xml;
	}
}