package org.jeesl.factory.ejb.module.mmg;

import java.time.LocalDateTime;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMmgItemFactory<L extends JeeslLang,
								MC extends JeeslMmgClassification<L,?,MC,?>,
								MG extends JeeslMmgGallery<L>,
								MI extends JeeslMmgItem<L,MG,?,USER>,
								USER extends JeeslSimpleUser>
								
{
	final static Logger logger = LoggerFactory.getLogger(EjbMmgItemFactory.class);
	
	private final Class<MI> cItem;

    public EjbMmgItemFactory(final Class<MI> cItem)
    {
        this.cItem = cItem;
    }
	
	public MI build(MG gallery)
	{
		try
		{
			MI ejb = cItem.newInstance();
			ejb.setVisible(true);
			ejb.setGallery(gallery);
			ejb.setLdtUpload(LocalDateTime.now());
			
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
	
	public void converter(JeeslFacade facade, MI ejb)
	{
		
	}
}