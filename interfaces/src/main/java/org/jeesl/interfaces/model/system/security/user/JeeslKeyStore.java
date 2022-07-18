package org.jeesl.interfaces.model.system.security.user;

import java.io.Serializable;
import java.util.Map;

import javax.crypto.SecretKey;

import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;

public interface JeeslKeyStore <KEY extends JeeslIoCryptoKey<?,?>,
								KT extends JeeslIoCryptoKeyState<?,?,KT,?>,
								ST extends JeeslIoCryptoStoreType<?,?,ST,?>
								>
				extends Serializable
{
	ST getType();
	Map<KEY,KT> getMapState();
	
	void update(KEY key, KT sate, SecretKey secret);
	
	SecretKey getSecretKey(KEY key);	
}