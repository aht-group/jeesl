package org.jeesl.factory.json.io.ssi.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.jeesl.factory.txt.io.crypto.TxtCryptoFactory;
import org.jeesl.model.json.io.crypto.JsonCryptoContainer;
import org.jeesl.model.json.io.crypto.JsonCryptoData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonCryptoDataFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonCryptoDataFactory.class);
	
	public static JsonCryptoData build(){return new JsonCryptoData();}
	
	public static <E extends Enum<E>> JsonCryptoData build(E code, String value)
	{
		JsonCryptoData json = build();
		json.setCode(code.toString());
		json.setValue(value);
		return json;
	}
	
	public static <E extends Enum<E>> void write(JsonCryptoContainer container, SecretKey secret, IvParameterSpec iv, MessageDigest md, E code, String value) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException
	{
		md.update(value.getBytes());
		container.getDatas().add(JsonCryptoDataFactory.build(code,TxtCryptoFactory.encrypt(TxtCryptoFactory.encrpytionAlgorithm,value,secret,iv)));
	}
}