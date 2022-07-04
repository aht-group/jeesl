package org.jeesl.factory.ejb.io.crypto;

import java.time.LocalDateTime;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCryptoKeyFactory <KEY extends JeeslIoCryptoKey<USER>,
								USER extends JeeslSimpleUser>
{
	final static Logger logger = LoggerFactory.getLogger(EjbCryptoKeyFactory.class);
	
	private final Class<KEY> cKey;
    
	public EjbCryptoKeyFactory(final Class<KEY> cKey)
	{       
        this.cKey = cKey;
	}
    
	public KEY build(USER user)
	{
		KEY ejb = null;
		try
		{
			ejb = cKey.newInstance();
			ejb.setUser(user);
			ejb.setRecord(LocalDateTime.now());
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void converter(JeeslFacade facade, KEY key)
	{
		
	}
}