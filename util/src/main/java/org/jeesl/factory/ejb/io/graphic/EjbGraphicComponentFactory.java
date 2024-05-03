package org.jeesl.factory.ejb.io.graphic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbGraphicComponentFactory<G extends JeeslGraphic<?,GC,GS>,
								GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<?,?,GS,G>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbGraphicComponentFactory.class);
	
	private final Class<GC> cComponent;
	
    public EjbGraphicComponentFactory(final Class<GC> cComponent)
    {
        this.cComponent = cComponent;
    } 
        
	public GC build(G graphic, GS style, boolean css, double size, String color, double offsetX, double offsetY, double rotation)
	{
		GC ejb = null;
        try
        {
			ejb=cComponent.newInstance();
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
			ejb=cComponent.newInstance();
			ejb.setGraphic(graphic);
		}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        
        return ejb;
    }
	
	public <T extends EjbWithGraphic<G>> BidiMap<T,GC> toBidiMap(List<T> owners, List<GC> components)
	{
		Map<Long,T> mapOwners = new HashMap<>();
		for(T t : owners) {mapOwners.put(t.getGraphic().getId(),t);}
		BidiMap<T,GC> map = new DualHashBidiMap<>();
		for(GC c : components)
		{
			if(mapOwners.containsKey(c.getGraphic().getId()))
			{
				map.put(mapOwners.get(c.getGraphic().getId()),c);
			}
		}
		
		return map;
	}
}