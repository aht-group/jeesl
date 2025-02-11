package org.jeesl.client.handler.io.fr;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Random;

import javax.naming.NamingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.exlp.controller.handler.io.log.LoggedExit;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.io.JsonUtil;
import org.jeesl.client.JeeslBootstrap;
import org.jeesl.client.controller.factory.JeeslFactoryProvider;
import org.jeesl.controller.handler.system.io.fr.storage.FileRepositoryAmazonS3;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.fr.EjbIoFrContainerFactory;
import org.jeesl.factory.ejb.io.fr.EjbIoFrMetaFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryStore;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.io.fr.IoFileMeta;
import org.jeesl.model.ejb.io.fr.IoFileStorage;
import org.jeesl.model.ejb.io.fr.IoFileType;
import org.jeesl.model.json.io.fr.JsonFrAmazonS3;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

public class CliFileRepository
{
	final static Logger logger = LoggerFactory.getLogger(CliFileRepository.class);
	
	private final Configuration config;
	
	private EjbIoFrContainerFactory<IoFileStorage,IoFileContainer> efContainer; 
	private EjbIoFrMetaFactory<IoFileContainer,IoFileMeta,IoFileType> efMeta; 
	private JeeslFileRepositoryStore<IoFileMeta> frFile;
	
	private final String s3ObjectIdentifier = "1234567890.svg";
	private final Path pFile;
	
	public CliFileRepository(Configuration config) throws NamingException
	{
		this.config=config;
		efContainer = JeeslFactoryProvider.ioFileRepository().ejbContainer();
		efMeta = JeeslFactoryProvider.ioFileRepository().ejbMeta();
		
		pFile = Paths.get(config.getString("net.amazon.s3.file"));
	}
	
	private AmazonS3 s3ClientV1()
	{
		System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");
		
		String s3Id = config.getString("net.amazon.s3.id");
		String s3Key = config.getString("net.amazon.s3.key");
		
		logger.info("accessKey: "+s3Id);
		logger.info("secretKey: "+s3Key);
		
		return FileRepositoryAmazonS3.s3ClientV1(s3Id,s3Key);
	}
	
	private void testS3v1() throws IOException
	{
		AmazonS3 s3Client = s3ClientV1();
		
		String s3Bucket = config.getString("net.amazon.s3.bucket");
		
		Path pTmp = Paths.get(config.getString("dir.tmp"));
		
		logger.info("S3.bucket: "+s3Bucket);
		logger.info("S3.file: "+pFile.toString());
		logger.info("Local.Tmp: "+pFile.toString());
		
		byte[] bytes = Files.readAllBytes(pFile);
		
		ObjectMetadata om = new ObjectMetadata();
		om.setContentLength(bytes.length);
		byte[] md5 = DigestUtils.md5(bytes);
		om.setContentMD5(new String(Base64.encodeBase64(md5)));
		
		InputStream is = new ByteArrayInputStream(bytes);
		s3Client.putObject(new PutObjectRequest(s3Bucket, s3ObjectIdentifier, is, om));
		
		S3Object s3object = s3Client.getObject(s3Bucket, s3ObjectIdentifier);
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		
		File fDst = new File(pTmp.toFile(),"out.svg");
		logger.info(fDst.toString());
		FileUtils.copyInputStreamToFile(inputStream, fDst);
	}
	
	public void testS3v2Read() throws IOException
	{
		String s3Id = config.getString("net.amazon.s3.id");
		String s3Key = config.getString("net.amazon.s3.key");
		String s3Bucket = config.getString("net.amazon.s3.bucket");
		
		S3Client s3Client = S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.EU_CENTRAL_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(s3Id,s3Key)))
                .build();
		
		FileInputStream fileIs = new FileInputStream(pFile.toFile());
		software.amazon.awssdk.services.s3.model.PutObjectRequest putObjectRequest = software.amazon.awssdk.services.s3.model.PutObjectRequest.builder()
                .bucket(s3Bucket)
                .key(s3ObjectIdentifier)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileIs,fileIs.available()));
        logger.info("Uploaded "+pFile.toString());
		
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
	            .bucket(s3Bucket)
	            .key(s3ObjectIdentifier)
	            .build();
		software.amazon.awssdk.core.ResponseInputStream<software.amazon.awssdk.services.s3.model.GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
	    InputStream inputStream = s3Object;
	    
	    Path pTmp = Paths.get(config.getString("dir.tmp"));
	    File fDst = new File(pTmp.toFile(),"out.svg");
	    FileUtils.copyInputStreamToFile(inputStream, fDst);
	}
	
	public void file() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		JsonFrAmazonS3 json = new JsonFrAmazonS3();
		json.setBucket("xx");
		json.setId("xx");
		json.setKey("xx/xx");
		logger.info(JsonUtil.toStringSilent(json));
		
		LoggedExit.exit(true);
		
		IoFileStorage storage = new IoFileStorage();
		storage.setJson(JsonUtil.toStringSilent(json));
		frFile = new FileRepositoryAmazonS3<IoFileStorage,IoFileMeta>(storage);
		
		Random rnd = new Random();
		for(int i=0;i<20;i++)
		{
			int s=10;
			while(s<100000000)
			{
				IoFileContainer container = efContainer.build(storage);
				IoFileMeta meta = efMeta.build(container, "", 0, LocalDateTime.now());
				byte[] expecteds = new byte[s];
				rnd.nextBytes(expecteds);
				frFile.saveToFileRepository(meta, expecteds);
				
				byte[] actuals = frFile.loadFromFileRepository(meta);
				Assertions.assertArrayEquals(expecteds, actuals);
				s=s*100;
			}
		}
	}
	
    public static void main(String[] args) throws Exception
    {
    	Configuration config = JeeslBootstrap.wrap();
    	CliFileRepository cli = new CliFileRepository(config);
    	
//    	cli.testS3v1();
    	
    	cli.testS3v2Read();
    	
//    	cli.file();
    }
}