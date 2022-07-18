package org.jeesl.model.ejb.system.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.jeesl.interfaces.model.system.security.user.JeeslKeyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSessionKeystore <KEY extends JeeslIoCryptoKey<?,?>,
												KT extends JeeslIoCryptoKeyState<?,?,KT,?>,
												ST extends JeeslIoCryptoStoreType<?,?,ST,?>>
						implements JeeslKeyStore<KEY,KT,ST>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSessionKeystore.class);
	public static final long serialVersionUID=1;
	
	private final Map<KEY,SecretKey> mapKey;
	private final Map<KEY,KT> mapState; @Override public Map<KEY,KT> getMapState() {return mapState;}

	private final List<KEY> keys; public List<KEY> getKeys() {return keys;}

	private ST type; public ST getType() {return type;} public void setType(ST type) {this.type = type;}

	public AbstractSessionKeystore()
	{
		mapKey = new HashMap<>();
		mapState = new HashMap<>();
		keys = new ArrayList<>();
	}
	
	@Override public void update(KEY key, KT state, SecretKey secret)
	{
		if(!keys.contains(key)) {keys.add(key);}
//		AbstractSessionKeyStore.getKeyFromPassword(mapPassword.get(key),key.getPwdSalt());
		if(secret!=null){mapKey.put(key,secret);}
		mapState.put(key,state);
	}
	
	
	@Override public SecretKey getSecretKey(KEY key)
	{
		if(mapKey.containsKey(key)) {return mapKey.get(key);}
		return null;
	}
	
	public boolean isUnlocked(KEY key)
	{
		return mapKey.containsKey(key);
	}
	
	public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		return secret;
	}
}