package org.jeesl.factory.ejb.util;

import java.util.Date;

import org.jeesl.interfaces.model.io.label.revision.data.JeeslLastUpdate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbLastUpdateFactory
{
	final static Logger logger = LoggerFactory.getLogger(EjbLastUpdateFactory.class);
    
	public static <T extends JeeslLastUpdate<USER>, USER extends JeeslUser<?>> void update(USER user, T ejb)
	{       
       ejb.setLastUpdateBy(user);
       ejb.setLastUpdateAt(new Date());
	}
}