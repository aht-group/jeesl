package org.jeesl.interfaces.model.io.crypto;

import java.io.Serializable;
import java.util.Map;

import javax.crypto.SecretKey;

public interface JeeslIoCryptoStore <KEY extends JeeslIoCryptoKey<?,?>,
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