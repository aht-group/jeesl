package org.jeesl.factory.ejb.io.graphic;

import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbGraphicFigureFactory<L extends JeeslLang, D extends JeeslDescription,
								G extends JeeslGraphic<?,GC,GS>,
								GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbGraphicFigureFactory.class);
	
	private final Class<GC> cF;
	
    public EjbGraphicFigureFactory(final Class<GC> cF)
    {
        this.cF = cF;
    } 
        
	public GC build(G graphic, GS style, boolean css, double size, String color, double offsetX, double offsetY, double rotation)
	{
		GC ejb = null;
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
	
	public GC build(G graphic)
	{
		GC ejb = null;
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