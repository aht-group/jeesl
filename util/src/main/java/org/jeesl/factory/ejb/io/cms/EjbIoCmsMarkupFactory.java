package org.jeesl.factory.ejb.io.cms;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoCmsMarkupFactory<M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<?,?,MT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoCmsMarkupFactory.class);
	
	private final Class<M> cMarkup;
	
	public static <M extends JeeslIoMarkup<MT>, MT extends JeeslIoMarkupType<?,?,MT,?>>
			 EjbIoCmsMarkupFactory<M,MT> instance(final Class<M> cMarkup)
	 {
		return new EjbIoCmsMarkupFactory<>(cMarkup);
	 }
	
    public EjbIoCmsMarkupFactory(final Class<M> cMarkup)
    {
        this.cMarkup = cMarkup;
    } 
 
    public M build(MT type)
    {
    	M markup = null;
		try
		{
			markup = cMarkup.newInstance();
	    	markup.setLkey(JeeslLocale.none);
	    	markup.setType(type);
	    	markup.setContent("");
		}
		catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}

    	return markup;
    }
}