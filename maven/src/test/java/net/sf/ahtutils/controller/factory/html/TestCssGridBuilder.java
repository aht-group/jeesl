package net.sf.ahtutils.controller.factory.html;

import java.io.File;
import java.io.IOException;

import net.sf.ahtutils.test.AbstractUtilsMavenTst;
import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.dir.DirChecker;

import org.apache.commons.io.FileUtils;
import org.jeesl.JeeslMavenBootstrap;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCssGridBuilder extends AbstractUtilsMavenTst
{
	final static Logger logger = LoggerFactory.getLogger(TestCssGridBuilder.class);
	
	private CssGridBuilder gridFactory;
	
	protected static File fTmpDir;
	private File fResource;
	
	@BeforeClass
	public static void initTmpDir()
	{
		fTmpDir = new File(fTarget,"cssTmp");
		if(!fTmpDir.exists()){fTmpDir.mkdir();}
	}
	
	@Before
	public void init()
	{	
		fResource = new File(fTarget,"cssGrid");
		fResource.mkdir();
		gridFactory = new CssGridBuilder(fTmpDir,fResource);
	}
	
	@After
	public void clean() throws IOException
	{	
		if(fResource.isDirectory()){FileUtils.deleteDirectory(fResource);}
		else if(fResource.isFile()){fResource.delete();}
		gridFactory = null;
	}
	
	@Test(expected=ExlpConfigurationException.class)
	public void noDir() throws ExlpConfigurationException
	{
		fResource.delete();
		DirChecker.checkFileIsDirectory(fResource);
	}
	
	@Test(expected=ExlpConfigurationException.class)
	public void isFile() throws IOException, ExlpConfigurationException
	{
		FileUtils.deleteDirectory(fResource);
		fResource.createNewFile();
		DirChecker.checkFileIsDirectory(fResource);
	}
	
	@Test
	public void create()
	{
		gridFactory.buildCss(75,5);
	}
	
	public static void main(String[] args) throws Exception
    {
		JeeslMavenBootstrap.init();
		
		TestCssGridBuilder.initTmpDir();
		TestCssGridBuilder test = new TestCssGridBuilder();
		test.setSaveReference(true);
		test.init();
		test.create();
    }
}