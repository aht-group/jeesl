package org.jeesl.factory.txt.io.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.jeesl.model.ejb.system.security.AbstractSessionKeystore;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliTxtCryptoFactory
{
	final static Logger logger = LoggerFactory.getLogger(CliTxtCryptoFactory.class);

	public CliTxtCryptoFactory()
	{
		
	}
	
	public void cli() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException
	{
		String plainText = "www.jeesl.org";
	    
		String pwdSecret = "jeesl";
	    String pwdSalt = "1234";
	    
	    String memoIv = TxtCryptoFactory.buildIv();
	    memoIv = "BNrK91cdNzYTy9BszXkkpQ==";
	    
	    SecretKey key = TxtCryptoFactory.getKeyFromPassword(pwdSecret,pwdSalt);
	    IvParameterSpec iv = TxtCryptoFactory.buildIv(memoIv);
	    
	    String cipherText = TxtCryptoFactory.encrypt(TxtCryptoFactory.encrpytionAlgorithm,plainText,key,iv);
	    String decryptedCipherText = TxtCryptoFactory.decrypt(TxtCryptoFactory.encrpytionAlgorithm,cipherText,key,iv);
	  
	    logger.info("Clear:    {}",plainText);
	    logger.info("IV:       {}",memoIv);
	    logger.info("Ciper:    {}",cipherText);
	    logger.info("Decrypted {}",decryptedCipherText);
	}

	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		CliTxtCryptoFactory cli = new CliTxtCryptoFactory();
		cli.cli();
	}
}