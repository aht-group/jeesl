package org.jeesl.factory.txt.io.graphic;

import java.awt.Color;

import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtGraphicComponentFactory <GC extends JeeslGraphicComponent<?,GC,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtGraphicComponentFactory.class);
	
	public static <GC extends JeeslGraphicComponent<?,GC,?>> TxtGraphicComponentFactory<GC> instance() {return new TxtGraphicComponentFactory<>();}
	
	public String toIntegerRgb(GC gc)
	{
		return TxtGraphicComponentFactory.toIntegerRgb(gc.getColor());
	}
	public Color toAwtColor(GC gc)
	{
        return TxtGraphicComponentFactory.toAwtColor(gc.getColor());
	}
	
	public static String toIntegerRgb(String hexRgb)
	{
		int r = Integer.valueOf(hexRgb.substring(0,2), 16);
        int g = Integer.valueOf(hexRgb.substring(2,4), 16);
        int b = Integer.valueOf(hexRgb.substring(4,6), 16);
        
        return String.format("%03d%03d%03d", r,g,b);
	}
	public static String toIntegerRgb(Color color)
	{
        return String.format("%03d%03d%03d", color.getRed(),color.getGreen(),color.getBlue());
	}
	
	public static Color toAwtColor(String hexRgb)
	{
		int r = Integer.valueOf(hexRgb.substring(0,2), 16);
        int g = Integer.valueOf(hexRgb.substring(2,4), 16);
        int b = Integer.valueOf(hexRgb.substring(4,6), 16);
        
        return new Color(r, g, b, 1);
	}
}