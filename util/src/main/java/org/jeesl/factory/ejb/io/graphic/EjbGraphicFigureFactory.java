package org.jeesl.factory.ejb.io.graphic;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicFigure;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbGraphicFigureFactory<L extends JeeslLang, D extends JeeslDescription,
								G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
								F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbGraphicFigureFactory.class);
	
	private final Class<F> cF;
	
    public EjbGraphicFigureFactory(final Class<F> cF)
    {
        this.cF = cF;
    } 
        
	public F build(G graphic, FS style, boolean css, double size, String color, double offsetX, double offsetY, double rotation)
	{
        F ejb = null;
        try
        {
			ejb=cF.newInstance();
			ejb.setGraphic(graphic);
			ejb.setStyle(style);
			
			ejb.setCss(css);
			ejb.setSize(size);
			ejb.setColor(color);
			ejb.setOffsetX(offsetX);
			ejb.setOffsetY(offsetY);
			ejb.setRotation(rotation);
		}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        
        return ejb;
    }
	
	public F build(G graphic)
	{
        F ejb = null;
        try
        {
			ejb=cF.newInstance();
			ejb.setGraphic(graphic);
		}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        
        return ejb;
    }
}