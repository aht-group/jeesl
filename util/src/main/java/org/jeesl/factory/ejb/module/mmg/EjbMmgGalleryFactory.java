package org.jeesl.factory.ejb.module.mmg;

import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMmgGalleryFactory<L extends JeeslLang,
									MG extends JeeslMmgGallery<L>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbMmgGalleryFactory.class);
	
	private final Class<MG> cGallery;

    public EjbMmgGalleryFactory(final Class<MG> cGallery)
    {
        this.cGallery = cGallery;
    }
	
	public MG build()
	{
		try
		{
			MG ejb = cGallery.newInstance();
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
}