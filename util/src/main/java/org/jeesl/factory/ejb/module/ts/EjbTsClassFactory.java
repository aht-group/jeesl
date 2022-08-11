package org.jeesl.factory.ejb.module.ts;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsClassFactory<CAT extends JeeslTsCategory<?,?,CAT,?>,
								EC extends JeeslTsEntityClass<?,?,CAT,ENTITY>,
								ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTsClassFactory.class);
	
	final Class<EC> cEc;
    
	public EjbTsClassFactory(final Class<EC> cEc)
	{       
        this.cEc=cEc;
	}
	    
	public EC build(CAT category)
	{
		EC ejb = null;
		try
		{
			ejb = cEc.newInstance();
			ejb.setPosition(1);
			ejb.setVisible(true);
			ejb.setCategory(category);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}