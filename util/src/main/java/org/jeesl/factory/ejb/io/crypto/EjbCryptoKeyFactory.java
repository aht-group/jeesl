package org.jeesl.factory.ejb.io.crypto;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.jeesl.factory.txt.io.crypto.TxtCryptoFactory;
import org.jeesl.factory.txt.system.security.TxtUserFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyStatus;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCryptoKeyFactory <KEY extends JeeslIoCryptoKey<USER,KS>,
								KS extends JeeslIoCryptoKeyStatus<?,?,KS,?>,
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
			ejb.setPwdSalt(TxtUserFactory.buildSalt());

			ejb.setMemoIv(TxtCryptoFactory.buildIv());
			
			byte[] clear = new byte[128];
			new SecureRandom().nextBytes(clear);
			ejb.setMemoText(Base64.getEncoder().encodeToString(clear));
			logger.info("String size: "+ejb.getMemoText().length());
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void converter(JeeslFacade facade, KEY key)
	{
		
	}
}