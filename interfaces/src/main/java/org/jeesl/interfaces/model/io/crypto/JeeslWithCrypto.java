package org.jeesl.interfaces.model.io.crypto;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithCrypto <KEY extends JeeslIoCryptoKey<?,?>> extends EjbWithId
{
	KEY getCryptoKey();
	void setCryptoKey(KEY key);
	
	String getCryptoJson();
	void setCryptoJson(String cryptoJson);
}