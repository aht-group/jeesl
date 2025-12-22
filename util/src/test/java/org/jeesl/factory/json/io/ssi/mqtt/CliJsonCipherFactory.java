package org.jeesl.factory.json.io.ssi.mqtt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.io.JsonUtil;
import org.jeesl.factory.txt.io.crypto.TxtCryptoFactory;
import org.jeesl.model.ejb.system.security.AbstractSessionKeystore;
import org.jeesl.model.json.io.ssi.mqtt.JsonMqttCipher;
import org.jeesl.model.json.io.ssi.mqtt.JsonMqttMessage;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

public class CliJsonCipherFactory
{	
	final static Logger logger = LoggerFactory.getLogger(CliJsonCipherFactory.class);

	private SecretKey key;
	private IvParameterSpec iv;
	
	public CliJsonCipherFactory() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException
	{
		String plainText = "www.jeesl.org";
	    
		String pwdSecret = "jeesl";
	    String pwdSalt = "1234";
	    
	    String memoIv = TxtCryptoFactory.buildIv();
	    
	    key = TxtCryptoFactory.getKeyFromPassword(pwdSecret,pwdSalt);
	    iv = TxtCryptoFactory.buildIv(memoIv);
	    
	    String cipherText = TxtCryptoFactory.encrypt(key,iv,plainText);
	    String decryptedCipherText = TxtCryptoFactory.decrypt(key,iv,cipherText);
	  
	    logger.info("Clear:    {}",plainText);
	    logger.info("IV:       {}",memoIv);
	    logger.info("Ciper:    {}",cipherText);
	    logger.info("Decrypted {}",decryptedCipherText);
	}
	
	public void test() throws InvalidKeyException, JsonProcessingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException 
	{
		JsonMqttMessage jMessage = new JsonMqttMessage();
        jMessage.setTopic("xx");
        jMessage.setTime(LocalDateTime.now());
        jMessage.setPayload("Payload");

        JsonMqttCipher jsonClear = JsonMqttCipherFactory.build(jMessage);
		JsonUtil.info(jsonClear);

		JsonMqttCipher jsonCipher = JsonMqttCipherFactory.encrypt(key,jsonClear);
		JsonUtil.info(jsonCipher);
		
		JsonMqttCipher jsonDecrypted = JsonMqttCipherFactory.decrypt(key, jsonCipher);
		JsonUtil.info(jsonDecrypted);
	}

	public static void main(String[] args) throws Exception
	{
		Configuration config = JeeslBootstrap.init();
		CliJsonCipherFactory cli = new CliJsonCipherFactory();

		cli.test();
	}
}