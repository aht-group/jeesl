package org.jeesl.factory.css;

import java.util.List;

import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CssColorFactory <G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<?,?,GT,G>,
								GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<?,?,GS,G>>
{
	final static Logger logger = LoggerFactory.getLogger(CssColorFactory.class);
    
	public static String colorGrey = "#F8F8FF";
	    
	private String build(GC figure)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#").append(figure.getColor()); 
		return sb.toString();
	}
	
	public String firstCss(G graphic)
	{
		return css(0,graphic.getFigures(),"");
	}
	
	public String css(int index, G graphic, String fallback)
	{
		return css(index,graphic.getFigures(),fallback);
	}
	
	private String css(int index, List<GC> figures, String fallback)
	{
		if(figures.size()>index)
		{
			return build(figures.get(index));
		}
		return fallback;
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
					SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
		void appendColor(StringBuilder sb, LIGHT light)
	{
		if(sb!=null && light!=null)
		{
			sb.append(" background: #").append(light.getColorBackground()).append(";");
			sb.append(" color: #").append(light.getColorText()).append(";");
		}
	}
}