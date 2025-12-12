package org.jeesl.factory.json.io.ssi.mqtt;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.exlp.util.io.JsonUtil;
import org.jeesl.factory.txt.io.crypto.TxtCryptoFactory;
import org.jeesl.model.json.io.ssi.mqtt.JsonMqttCipher;
import org.jeesl.model.json.io.ssi.mqtt.JsonMqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMqttCipherFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonMqttCipherFactory.class);
	
	public static JsonMqttCipher build()
	{
		JsonMqttCipher json = new JsonMqttCipher();
		return json;
	}
	
	public static JsonMqttCipher build(JsonMqttMessage message)
	{
		JsonMqttCipher json = new JsonMqttCipher();
		json.setMessages(new ArrayList<>());
		json.getMessages().add(message);
		return json;
	}
	
	public static JsonMqttCipher encrypt(SecretKey key, JsonMqttCipher json)
	{
		try
		{
			JsonMqttCipher cipherJson = JsonMqttCipherFactory.build();
			cipherJson.setIv(TxtCryptoFactory.buildIv());
			IvParameterSpec iv = TxtCryptoFactory.buildIv(cipherJson.getIv());
		
			cipherJson.setCipherText(TxtCryptoFactory.encrypt(key,iv,JsonUtil.toString(json)));
			return cipherJson;
		}
		catch (IOException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonMqttCipher decrypt(SecretKey key, JsonMqttCipher json)
	{
		try
		{
			IvParameterSpec iv = TxtCryptoFactory.buildIv(json.getIv());
			JsonMqttCipher clearJson = JsonUtil.read(JsonMqttCipher.class, TxtCryptoFactory.decrypt(key,iv,json.getCipherText()));
			return clearJson;
		}
		catch (IOException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}