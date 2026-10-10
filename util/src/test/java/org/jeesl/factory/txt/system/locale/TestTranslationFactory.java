package org.jeesl.factory.txt.system.locale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTranslationFactory extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestTranslationFactory.class);
	
	private static final String targetDir = "msg-bundle.test";
	private TranslationFactory tFactory;
	private File fDstDir;
	private String bundleName = "msg";
	
	@BeforeEach
	public void init() throws IOException
	{
		tFactory = new TranslationFactory();
		tFactory.setOutEncoding("UTF-8");
		
		fDstDir = new File(fDstDir,targetDir);
		if(fDstDir.exists())
		{
			FileUtils.deleteDirectory(fDstDir);
		}
		fDstDir.mkdir();
	}
	
	@Test
	public void targetCreation() throws FileNotFoundException, JeeslNotFoundException
    {	
		tFactory.add("src/test/resources/data/xml/msgBundle/translation1.xml");
		tFactory.writeMessageResourceBundles(bundleName,fDstDir);
		Assertions.assertTrue(fDstDir.exists());
		Assertions.assertTrue(fDstDir.isDirectory());
		
		Assertions.assertTrue(fDstDir.exists(),"Directory ("+fDstDir+") does not exist: "+fDstDir.getAbsolutePath());
		Assertions.assertTrue(fDstDir.isDirectory(),"("+fDstDir+") not a directory: "+fDstDir.getAbsolutePath());
    }
	
	@Test
	public void langFiles() throws FileNotFoundException, JeeslNotFoundException
    {	
		tFactory.add("src/test/resources/data/xml/msgBundle/translation1.xml");
		tFactory.writeMessageResourceBundles(bundleName,fDstDir);
		TranslationMap tMap = tFactory.gettMap();
		for(String s : tMap.getLangKeys())
		{
			File f = new File(fDstDir,bundleName+"_"+s+"."+TranslationFactory.msgBundleSuffix);
			Assertions.assertTrue(f.exists(),"Should exist: "+f.getAbsolutePath());
			Assertions.assertTrue(f.isFile());
		}
    }
	
	@Test
	public void multipleTranslationFiles() throws FileNotFoundException, JeeslNotFoundException
    {	
		tFactory.add("src/test/resources/data/xml/msgBundle/translation1.xml");
		tFactory.add("src/test/resources/data/xml/msgBundle/translation2.xml");
		tFactory.writeMessageResourceBundles(bundleName,fDstDir);
		TranslationMap tMap = tFactory.gettMap();
		Assertions.assertEquals(2,tMap.getLangKeys().size());
		for(String s : tMap.getLangKeys())
		{
			Assertions.assertEquals(3,tMap.getTranslationKeys(s).size());
		}
    }
	
	@Test
	public void rekursiveDirectory() throws FileNotFoundException, JeeslNotFoundException
    {	
		tFactory.rekursiveDirectory("src/test/resources/data/xml/msgBundle");
		tFactory.writeMessageResourceBundles(bundleName,fDstDir);
		TranslationMap tMap = tFactory.gettMap();
		Assertions.assertEquals(2,tMap.getLangKeys().size());
		for(String s : tMap.getLangKeys())
		{
			Assertions.assertEquals(5,tMap.getTranslationKeys(s).size());
		}
    }
}
