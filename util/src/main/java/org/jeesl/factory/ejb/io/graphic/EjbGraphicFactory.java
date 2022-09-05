package org.jeesl.factory.ejb.io.graphic;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.openfuxml.content.media.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbGraphicFactory<L extends JeeslLang, D extends JeeslDescription,
								G extends JeeslGraphic<L,D,GT,F,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
								F extends JeeslGraphicComponent<L,D,G,GT,F,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbGraphicFactory.class);
	
	private final Class<G> cGrpahic;
	private final Class<GT> cType;
	private final Class<GS> cShape;
	
    public EjbGraphicFactory(final Class<G> cGrpahic, final Class<GT> cType, final Class<GS> cShape)
    {
        this.cGrpahic = cGrpahic;
        this.cType = cType;
        this.cShape = cShape;
    } 
        
	public G build(GT type)
	{
        G ejb = null;
        try
        {
			ejb=cGrpahic.newInstance();
			ejb.setType(type);
		}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        
        return ejb;
    }
	
	public G buildSymbol(GT type, GS style)
	{
        G ejb = null;
        try
        {
			ejb=cGrpahic.newInstance();
			ejb.setType(type);
			ejb.setStyle(style);
			ejb.setSize(5);
			ejb.setColor("aaaaaa");
		}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        
        return ejb;
    }

	public static <T extends EjbWithId> String toCacheKey(T t)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(t.getClass().getName());
		sb.append(":");
		sb.append(t.getId());
		return sb.toString();
	}
	public static String toCacheKey(Image image)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(image.getVersion());
		sb.append(":");
		sb.append(image.getId());
		return sb.toString();
	}
	
	public void converter(JeeslFacade facade, G ejb)
	{
		if(ejb.getType()!=null) {ejb.setType(facade.find(cType, ejb.getType()));}
		if(ejb.getStyle()!=null) {ejb.setStyle(facade.find(cShape, ejb.getStyle()));}
	}
}