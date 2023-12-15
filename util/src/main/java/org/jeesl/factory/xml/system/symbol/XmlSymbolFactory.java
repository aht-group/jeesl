package org.jeesl.factory.xml.system.symbol;

import java.util.Objects;

import org.jeesl.factory.xml.system.status.XmlStyleFactory;
import org.jeesl.factory.xml.system.status.XmlStylesFactory;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.io.graphic.Symbol;
import org.jeesl.model.xml.io.locale.status.Styles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSymbolFactory <L extends JeeslLang, D extends JeeslDescription,
								G extends JeeslGraphic<GT,?,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
								GS extends JeeslGraphicShape<L,D,GS,G>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSymbolFactory.class);
		
	private Symbol q;
	
	private XmlStyleFactory<L,D,GS> xfStyle;
	private XmlColorsFactory<G> xfColors;
	private XmlSizesFactory<G> xfSizes;
	
	public XmlSymbolFactory(String localeCode, Symbol q)
	{
		this.q=q;
		if(Objects.nonNull(q.getStyles()) && Objects.nonNull(q.getStyles().getStyle())) {xfStyle = new XmlStyleFactory<>(localeCode,q.getStyles().getStyle().get(0));}
		if(Objects.nonNull(q.getColors())) {xfColors = new XmlColorsFactory<>(q.getColors());}
		if(Objects.nonNull(q.getSizes())) {xfSizes = new XmlSizesFactory<>(q.getSizes());}
	}
	
	public Symbol build(G graphic)
	{
		Symbol xml = build();
		
		if(Objects.nonNull(q.getStyles()))
		{
			Styles styles = XmlStylesFactory.build();
			
			if(graphic.getStyle()!=null){styles.getStyle().add(xfStyle.build(JeeslGraphicShape.Group.outer,graphic.getStyle()));}
			
			xml.setStyles(styles);
		}
		
		if(Objects.nonNull(q.getColors())) {xml.setColors(xfColors.build(graphic));}
		if(Objects.nonNull(q.getSizes())) {xml.setSizes(xfSizes.build(graphic));}
		
		return xml;
	}
	
	public static Symbol build()
	{
		Symbol xml = new Symbol();
		return xml;
	}
}