package org.jeesl.factory.ejb.io.label.revision.data;

import java.time.LocalDateTime;
import java.util.Date;

import org.jeesl.interfaces.model.io.label.revision.data.JeeslLastModified;
import org.jeesl.interfaces.model.io.label.revision.data.JeeslLastUpdate;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbLastModifiedFactory
{
	final static Logger logger = LoggerFactory.getLogger(EjbLastModifiedFactory.class);
    
	public static <T extends JeeslLastModified<USER>, USER extends JeeslSimpleUser> void modified(USER user, T ejb)
	{       
       ejb.setLastModifiedBy(user);
       ejb.setLastModifiedAt(LocalDateTime.now());
	}
	
	public static <T extends JeeslLastUpdate<USER>, USER extends JeeslUser<?>> void update(USER user, T ejb)
	{       
       ejb.setLastUpdateBy(user);
       ejb.setLastUpdateAt(new Date());
	}
}