package net.sf.ahtutils.controller.factory.java.security;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;
import net.sf.ahtutils.exception.processing.UtilsConfigurationException;
import net.sf.ahtutils.xml.access.Category;
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

public class TestJavaSecuritySeamPagesFactory extends AbstractJavaSecurityFactoryTst
{
	final static Logger logger = LoggerFactory.getLogger(TestJavaSecuritySeamPagesFactory.class);
	
	private JavaSecuritySeamPagesFactory seamPagesFactory;
	
	private File fPackage,fSrcDir;
	private String classPrefix;
	
	@Before
	public void init()
	{	
		classPrefix = "Utils";
		fPackage = new File(fTarget,"seamPagesFactory");
		fPackage.mkdir();
		fSrcDir = new File(fPackage,"src");
		fSrcDir.mkdir();
		mkPackages(fSrcDir);
		seamPagesFactory = new JavaSecuritySeamPagesFactory(fTmpDir,classPrefix,fSrcDir,"myLogin.jsf","myDenied.jsf","my.base.pack");
	}
	
	private void mkPackages(File fSrcDir)
	{
		for(Category c : lC)
		{
			for(View v : c.getViews().getView())
			{
				String fDir = v.getNavigation().getPackage().replaceAll("\\.", "/");
				File f = new File(fSrcDir,fDir);
				f.mkdirs();
			}
		}
	}
	
	@After
	public void clean() throws IOException
	{	
		if(fPackage.isDirectory()){FileUtils.deleteDirectory(fPackage);}
		else if(fPackage.isFile()){fPackage.delete();}
		seamPagesFactory = null;
	}
	
	@Test(expected=ExlpConfigurationException.class)
	public void noDir() throws ExlpConfigurationException, IOException
	{
		FileUtils.deleteDirectory(fPackage);
		DirChecker.checkFileIsDirectory(fPackage);
	}
	
	@Test(expected=UtilsConfigurationException.class)
	public void withoutNavigation() throws UtilsConfigurationException, IOException, TemplateException
	{
		lC.get(0).getViews().getView().get(0).setNavigation(null);
		seamPagesFactory.processCategories(lC);
	}
	
	@Test(expected=UtilsConfigurationException.class)
	public void withoutNavigationPackage() throws UtilsConfigurationException, IOException, TemplateException
	{
		lC.get(0).getViews().getView().get(0).getNavigation().setPackage(null);
		seamPagesFactory.processCategories(lC);
	}
	
	@Test(expected=UtilsConfigurationException.class)
	public void withoutNavigationViewPattern() throws UtilsConfigurationException, IOException, TemplateException
	{
		lC.get(0).getViews().getView().get(0).getNavigation().setViewPattern(null);
		seamPagesFactory.processCategories(lC);
	}
	
	@Test(expected=UtilsConfigurationException.class)
	public void withoutNavigatioUrlMapping() throws UtilsConfigurationException, IOException, TemplateException
	{
		lC.get(0).getViews().getView().get(0).getNavigation().setViewPattern(null);
		seamPagesFactory.processCategories(lC);
	}
	
	@Test(expected=UtilsConfigurationException.class)
	public void withoutTargetDir() throws UtilsConfigurationException, IOException, TemplateException
	{
		String tPackage = lC.get(0).getViews().getView().get(0).getNavigation().getPackage();
		File fP = new File(fSrcDir,tPackage.replaceAll("\\.", "/"));
		logger.debug("tdel: "+fP);
		FileUtils.deleteDirectory(fP);
		seamPagesFactory.processCategories(lC);
	}
	
	@Test
	public void mapContent() throws UtilsConfigurationException, IOException, TemplateException
	{
		seamPagesFactory.processCategories(lC);
		Assert.assertEquals(2, seamPagesFactory.getmViews().size());
		Assert.assertEquals(v1, seamPagesFactory.getmViews().get(v1.getNavigation().getPackage()).get(0));
		Assert.assertEquals(v2, seamPagesFactory.getmViews().get(v2.getNavigation().getPackage()).get(1));
		Assert.assertEquals(v3, seamPagesFactory.getmViews().get(v3.getNavigation().getPackage()).get(0));
	}
	
//	@Rule public IgnoreOtherRule test = new IgnoreOtherRule("process");
	@Test
	public void process() throws UtilsConfigurationException, IOException, TemplateException
	{
		seamPagesFactory.processCategories(lC);
	}
}