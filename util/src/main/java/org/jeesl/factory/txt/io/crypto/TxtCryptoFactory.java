package org.jeesl.factory.txt.io.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtCryptoFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtCryptoFactory.class);
	
	public static String encrpytionAlgorithm = "AES/CBC/PKCS5Padding";
	public static String hashAlgorithm = "SHA-256";
	
	public static String buildIv()
	{
		 byte[] iv = new byte[16];
		 new SecureRandom().nextBytes(iv);
		 return Base64.getEncoder().encodeToString(iv);
	}
	
	public static IvParameterSpec buildIv(String salt)
	{
	    byte[] iv = Base64.getDecoder().decode(salt.getBytes());
//	    logger.info("IV Length:"+iv.length);
	    return new IvParameterSpec(iv);
	}
	public static String encrypt(SecretKey key, IvParameterSpec iv, String clearText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		return TxtCryptoFactory.encrypt(TxtCryptoFactory.encrpytionAlgorithm, clearText, key, iv);
	}
	public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] cipherText = cipher.doFinal(input.getBytes());
		return Base64.getEncoder().encodeToString(cipherText);
	}
	
	public static String decrypt(SecretKey key, IvParameterSpec iv, String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		return TxtCryptoFactory.decrypt(TxtCryptoFactory.encrpytionAlgorithm, cipherText, key, iv);
	}
	public static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		return new String(plainText);
	}
}