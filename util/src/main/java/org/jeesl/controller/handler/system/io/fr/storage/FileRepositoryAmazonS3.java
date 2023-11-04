package org.jeesl.controller.handler.system.io.fr.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.exlp.util.io.JsonUtil;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryStore;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.model.json.io.fr.JsonFrAmazonS3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class FileRepositoryAmazonS3<STORAGE extends JeeslFileStorage<?,?,?,?,?>,
									META extends JeeslFileMeta<?,?,?,?>>
	implements JeeslFileRepositoryStore<META>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(FileRepositoryAmazonS3.class);

	private AmazonS3 s3client;
	private JsonFrAmazonS3 json; 
	
	public FileRepositoryAmazonS3(STORAGE storage)
	{
		try
		{
			json = JsonUtil.read(JsonFrAmazonS3.class,storage.getJson());
			AWSCredentials credentials = new BasicAWSCredentials(json.getId(), json.getKey());
			s3client = AmazonS3ClientBuilder
					  .standard()
					  .withCredentials(new AWSStaticCredentialsProvider(credentials))
					  .withRegion(Regions.EU_CENTRAL_1)
					  .build();
		}
		catch (IOException e) {e.printStackTrace();}
		
	}
	
	@Override public META saveToFileRepository(META meta, byte[] bytes) throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(meta.getCode());
		
		ObjectMetadata om = new ObjectMetadata();
		om.setContentLength(bytes.length);
		byte[] resultByte = DigestUtils.md5(bytes);
		String streamMD5 = new String(Base64.encodeBase64(resultByte));
		om.setContentMD5(streamMD5);
		
		InputStream is = new ByteArrayInputStream(bytes);
		
		s3client.putObject(new PutObjectRequest(json.getBucket(), meta.getCode(), is, om));
		
		return meta;
	}
	
	@Override
	public byte[] loadFromFileRepository(META meta) throws JeeslNotFoundException
	{
		S3Object s3object = s3client.getObject(json.getBucket(), meta.getCode());
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		try {return IOUtils.toByteArray(inputStream);}
		catch (IOException e) {throw new JeeslNotFoundException(e.getMessage());}
	}
	

	@Override public void delteFileFromRepository(META meta) throws JeeslConstraintViolationException, JeeslLockingException
	{
		s3client.deleteObject(json.getBucket(),meta.getCode());
	}
}