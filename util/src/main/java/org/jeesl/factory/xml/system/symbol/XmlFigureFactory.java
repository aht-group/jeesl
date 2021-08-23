package org.jeesl.factory.xml.system.symbol;

import org.jeesl.factory.xml.system.status.XmlStyleFactory;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicFigure;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.symbol.Figure;

public class XmlFigureFactory <L extends JeeslLang,D extends JeeslDescription,
								G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
								F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlFigureFactory.class);
		
	@SuppressWarnings("unused")
	private final Figure q;
	
	@SuppressWarnings("unused")
	private XmlStyleFactory<FS,L,D> xfStyle;
	
//	public XmlFigureFactory(Query query){this(query.getLang(),query.getGraphic());}
	public XmlFigureFactory(String localeCode, Figure q)
	{
		this.q=q;
		if(q.isSetStyle()){xfStyle = new XmlStyleFactory<FS,L,D>(localeCode,q.getStyle());}
	}
	
	public Figure build(F figure)
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