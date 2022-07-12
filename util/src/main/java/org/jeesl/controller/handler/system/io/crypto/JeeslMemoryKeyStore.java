package org.jeesl.controller.handler.system.io.crypto;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.system.security.user.JeeslKeyStore;
import org.jeesl.model.ejb.AbstractKeyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMemoryKeyStore<KEY extends JeeslIoCryptoKey<?,?>> implements Serializable,JeeslKeyStore<KEY>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(JeeslMemoryKeyStore.class);

	private static final Map<Long,SecretKey> map = new HashMap<>();
	
	public static <KEY extends JeeslIoCryptoKey<?,?>> JeeslMemoryKeyStore<KEY> instance(){return new JeeslMemoryKeyStore<>();}
	public JeeslMemoryKeyStore()
	{
		
	}
	
	
	@Override public void unlock(KEY key, String pwd)
	{	
		
		try
		{
			SecretKey secret = AbstractKeyStore.getKeyFromPassword(pwd,key.getPwdSalt());
			map.put(key.getId(),secret);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {e.printStackTrace();}
	}

	@Override public SecretKey getSecretKey(KEY key)
	{
		if(map.containsKey(key.getId())) {return map.get(key.getId());}
		return null;
	}
	
}