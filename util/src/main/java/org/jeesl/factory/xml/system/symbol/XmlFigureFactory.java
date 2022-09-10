package org.jeesl.factory.xml.system.symbol;

import org.jeesl.factory.xml.system.status.XmlStyleFactory;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.symbol.Figure;

public class XmlFigureFactory <L extends JeeslLang,D extends JeeslDescription,
								G extends JeeslGraphic<GT,GC,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
								GC extends JeeslGraphicComponent<G,GT,GC,FS>, FS extends JeeslGraphicShape<L,D,FS,G>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlFigureFactory.class);
		
	@SuppressWarnings("unused")
	private final Figure q;
	
	@SuppressWarnings("unused")
	private XmlStyleFactory<L,D,FS> xfStyle;
	
//	public XmlFigureFactory(Query query){this(query.getLang(),query.getGraphic());}
	public XmlFigureFactory(String localeCode, Figure q)
	{
		this.q=q;
		if(q.isSetStyle()){xfStyle = new XmlStyleFactory<>(localeCode,q.getStyle());}
	}
	
	public Figure build(GC figure)
	{
		Figure xml = build();
		
		xml.setColor(figure.getColor());
		
		return xml;
	}
	
	public static Figure build()
	{
		Figure xml = new Figure();
		return xml;
	}
}