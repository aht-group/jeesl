package org.jeesl.factory.ejb.module.mmg;

import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMmgItemFactory<L extends JeeslLang,
								MC extends JeeslMmgClassification<L,?,MC,?>,
								MG extends JeeslMmgGallery<L>,
								MI extends JeeslMmgItem<L,?>
								>
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
			
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
}