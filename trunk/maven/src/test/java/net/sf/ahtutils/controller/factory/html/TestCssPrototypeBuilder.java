package net.sf.ahtutils.controller.factory.html;

import java.io.File;
import java.io.IOException;

import net.sf.ahtutils.test.AbstractUtilsMavenTst;
import net.sf.ahtutils.test.UtilsMavenTstBootstrap;
import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.dir.DirChecker;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCssPrototypeBuilder extends AbstractUtilsMavenTst
{
	final static Logger logger = LoggerFactory.getLogger(TestCssPrototypeBuilder.class);
	
	private CssPrototypeBuilder cssBuilder;
	
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
		fResource = new File(fTarget,"cssPrototype");
		fResource.mkdir();
		cssBuilder = new CssPrototypeBuilder(fTmpDir,fResource,"aupCustom.css");
	}
	
//	@After
	public void clean() throws IOException
	{	
		if(fResource.isDirectory()){FileUtils.deleteDirectory(fResource);}
		else if(fResource.isFile()){fResource.delete();}
		cssBuilder = null;
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
		cssBuilder.buildCss("#A","#B","#C");
	}
	
	public static void main(String[] args) throws Exception
    {
		UtilsMavenTstBootstrap.init();
		
		TestCssPrototypeBuilder.initTmpDir();
		TestCssPrototypeBuilder test = new TestCssPrototypeBuilder();
		test.setSaveReference(true);
		test.init();
		test.create();
    }
}