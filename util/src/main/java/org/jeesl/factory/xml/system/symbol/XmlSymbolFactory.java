package org.jeesl.factory.xml.system.symbol;

import org.jeesl.factory.xml.system.status.XmlStyleFactory;
import org.jeesl.factory.xml.system.status.XmlStylesFactory;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Styles;
import net.sf.ahtutils.xml.symbol.Symbol;

public class XmlSymbolFactory <L extends JeeslLang, D extends JeeslDescription,
								G extends JeeslGraphic<GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
								F extends JeeslGraphicComponent<G,GT,F,FS>, FS extends JeeslGraphicShape<L,D,FS,G>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSymbolFactory.class);
		
	private Symbol q;
	
	private XmlStyleFactory<FS,L,D> xfStyle;
	private XmlColorsFactory<L,D,G,GT,F,FS> xfColors;
	private XmlSizesFactory<L,D,G,GT,F,FS> xfSizes;
	
	public XmlSymbolFactory(String localeCode, Symbol q)
	{
		this.q=q;
		if(q.isSetStyles() && q.getStyles().isSetStyle()){xfStyle = new XmlStyleFactory<FS,L,D>(localeCode,q.getStyles().getStyle().get(0));}
		if(q.isSetColors()){xfColors = new XmlColorsFactory<L,D,G,GT,F,FS>(q.getColors());}
		if(q.isSetSizes()){xfSizes = new XmlSizesFactory<L,D,G,GT,F,FS>(q.getSizes());}
	}
	
	public Symbol build(G graphic)
	{
		Symbol xml = build();
		
		if(q.isSetStyles())
		{
			Styles styles = XmlStylesFactory.build();
			
			if(graphic.getStyle()!=null){styles.getStyle().add(xfStyle.build(JeeslGraphicShape.Group.outer,graphic.getStyle()));}
			
			xml.setStyles(styles);
		}
		
		if(q.isSetColors()){xml.setColors(xfColors.build(graphic));}
		if(q.isSetStyles()){xml.setSizes(xfSizes.build(graphic));}
		
		return xml;
	}
	
	public static Symbol build()
	{
		Symbol xml = new Symbol();
		return xml;
	}
}