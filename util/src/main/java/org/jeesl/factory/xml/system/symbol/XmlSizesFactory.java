package org.jeesl.factory.xml.system.symbol;

import java.util.Objects;

import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.model.xml.io.graphic.Size;
import org.jeesl.model.xml.io.graphic.Sizes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSizesFactory <G extends JeeslGraphic<?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSizesFactory.class);
		
	private Sizes q;
	
	public XmlSizesFactory(Sizes q)
	{
		this.q=q;
		
	}
	
	public Sizes build(G graphic)
	{
		Sizes xml = build();
		
		if(Objects.nonNull(q.getSize()))
		{
			xml.getSize().add(XmlSizeFactory.build(JeeslGraphicShape.Size.outer, graphic.getSize()));
		}
		
		return xml;
	}
	
	public static Sizes build()
	{
		Sizes xml = new Sizes();
		return xml;
	}
	
	public static Sizes build(Size size)
	{
		Sizes xml = build();
		xml.getSize().add(size);
		return xml;
	}
}