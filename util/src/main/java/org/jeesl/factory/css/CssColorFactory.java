package org.jeesl.factory.css;

import java.util.List;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicFigure;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CssColorFactory
{
	final static Logger logger = LoggerFactory.getLogger(CssColorFactory.class);
    
	public static String colorGrey = "#F8F8FF";
	    
	public static <L extends JeeslLang, D extends JeeslDescription, G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>, F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
		String build(F figure)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#").append(figure.getColor());
		return sb.toString();
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription, G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>, F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
		String firstCss(G graphic)
	{
		return css(0,graphic.getFigures(),"");
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription, G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>, F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
		String css(int index, G graphic, String fallback)
	{
		return css(index,graphic.getFigures(),fallback);
	}
	
	private static <L extends JeeslLang, D extends JeeslDescription, G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>, F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
		String css(int index, List<F> figures, String fallback)
	{
		if(figures.size()>index)
		{
			return build(figures.get(index));
		}
		return fallback;
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription, LIGHT extends JeeslTrafficLight<L,D,SCOPE>,SCOPE extends JeeslStatus<L,D,SCOPE>>
		void appendColor(StringBuilder sb, LIGHT light)
	{
		if(sb!=null && light!=null)
		{
			sb.append(" background: #").append(light.getColorBackground()).append(";");
			sb.append(" color: #").append(light.getColorText()).append(";");
		}
	}
}