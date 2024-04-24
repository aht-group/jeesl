package org.jeesl.factory.txt.io.graphic;

import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtGraphicComponentFactory <GC extends JeeslGraphicComponent<?,GC,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtGraphicComponentFactory.class);
	
	public static String toIntegerRgb(String hexRgb)
	{
		int r = Integer.valueOf(hexRgb.substring(0,2), 16);
        int g = Integer.valueOf(hexRgb.substring(2,4), 16);
        int b = Integer.valueOf(hexRgb.substring(4,6), 16);
        
        return String.format("%03d%03d%03d", r,g,b);
	}
}