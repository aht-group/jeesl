package org.jeesl.model.ejb;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.system.security.user.JeeslKeyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractKeyStore <KEY extends JeeslIoCryptoKey<?,?>
//,										KS extends JeeslIoCryptoKeyStatus<?,?,KS,?>,
//										USER extends JeeslSimpleUser
										>
						implements JeeslKeyStore<KEY>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractKeyStore.class);
	public static final long serialVersionUID=1;
	
	private Map<KEY,String> mapPassword;
	
	public AbstractKeyStore()
	{		
		mapPassword = new HashMap<>();
	}
	
	@Override public void unlock(KEY key, String pwd)
	{
		logger.info("Unlocking ...");
	}
	
	public SecretKey getSecretKey(KEY key)
	{
		if(!mapPassword.containsKey(key)) {return null;}
		else
		{
			try
			{
				return AbstractKeyStore.getKeyFromPassword(mapPassword.get(key),key.getPwdSalt());
			}
			catch (NoSuchAlgorithmException | InvalidKeySpecException e)
			{
				logger.error(e.getMessage());
				return null;
			}
		}
	}
	
	public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		return secret;
	}
}