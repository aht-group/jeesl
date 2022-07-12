package org.jeesl.interfaces.model.system.security.user;

import java.io.Serializable;

import javax.crypto.SecretKey;

import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;

public interface JeeslKeyStore <KEY extends JeeslIoCryptoKey<?,?>
//,								KS extends JeeslIoCryptoKeyStatus<?,?,KS,?>,
//								USER extends JeeslSimpleUser
								>
				extends Serializable
{	
	void unlock(KEY key, String pwd);
	SecretKey getSecretKey(KEY key);
	
}