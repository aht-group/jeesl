package net.sf.ahtutils.controller.factory.java.security;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.sf.ahtutils.exception.processing.UtilsConfigurationException;
import net.sf.ahtutils.test.UtilsMavenTstBootstrap;
import net.sf.ahtutils.xml.access.View;
import net.sf.exlp.util.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.dir.DirChecker;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.TemplateException;

public class TestJavaSecurityIdentifierFactory extends AbstractJavaSecurityFactoryTst
{
	final static Logger logger = LoggerFactory.getLogger(TestJavaSecurityIdentifierFactory.class);
	
	private JavaSecurityViewIdentifierFactory idFactory;
	
	private File fPackage;
	private String classPrefix;
	
	@Before
	public void init()
	{	
		classPrefix = "Utils";
		fPackage = new File(fTarget,"idFactory");
		fPackage.mkdir();
		idFactory = new JavaSecurityViewIdentifierFactory(fTmpDir,fPackage,"net.sf.ahtutils",classPrefix);
	}
	
	@After
	public void clean() throws IOException
	{	
		if(fPackage.isDirectory()){FileUtils.deleteDirectory(fPackage);}
		else if(fPackage.isFile()){fPackage.delete();}
		idFactory = null;
	}
	
	@Test(expected=ExlpConfigurationException.class)
	public void noDir() throws ExlpConfigurationException
	{
		fPackage.delete();
		DirChecker.checkFileIsDirectory(fPackage);
	}
	
	@Test(expected=ExlpConfigurationException.class)
	public void isFile() throws IOException, ExlpConfigurationException
	{
		FileUtils.deleteDirectory(fPackage);
		fPackage.createNewFile();
		DirChecker.checkFileIsDirectory(fPackage);
	}
	
	@Test(expected=UtilsConfigurationException.class)
	public void categoryDirIsFile() throws UtilsConfigurationException, IOException, TemplateException
	{
		File actual = new File(fPackage,c1.getCode());
		actual.createNewFile();
		idFactory.create(c1);
	}
	
	@Test
	public void categoryDir() throws UtilsConfigurationException, IOException, TemplateException
	{
		idFactory.create(c1);
		File actual = new File(fPackage,c1.getCode());
		Assert.assertTrue(actual.exists());
		Assert.assertTrue(actual.isDirectory());
	}
	
	@Test
	public void createIdentifier() throws UtilsConfigurationException, IOException, TemplateException
	{
		idFactory.create(c1);
		File fSub = new File(fPackage,c1.getCode());
		for(View v : c1.getViews().getView())
		{
			File actual = new File(fSub,idFactory.createFileName(v.getCode()));
			Assert.assertTrue("File should exist: "+actual.getAbsolutePath(),actual.exists());
			Assert.assertTrue(actual.isFile());
			
			File expected = new File(rootDir,"aclIdentifier-"+v.getCode()+".java");
			assertText(expected, actual);
		}
	}
	
	@Test
	public void testClassNames()
	{
		List<String[]> tests = new ArrayList<String[]>();
		tests.add(new String[] {"test",classPrefix+"Test"});
		tests.add(new String[] {"Test",classPrefix+"Test"});
		tests.add(new String[] {"testMe",classPrefix+"TestMe"});
		
		for(String[] test : tests)
		{
			Assert.assertEquals(test[1], idFactory.createClassName(test[0]));
		}
	}
	
	public static void main(String[] args) throws Exception
    {
		UtilsMavenTstBootstrap.init();
		
		TestJavaSecurityIdentifierFactory test = new TestJavaSecurityIdentifierFactory();
		test.setSaveReference(true);
		test.initViews();
		test.init();
		test.createIdentifier();
    }
}