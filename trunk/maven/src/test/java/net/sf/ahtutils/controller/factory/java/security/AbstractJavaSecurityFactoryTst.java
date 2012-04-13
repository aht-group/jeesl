package net.sf.ahtutils.controller.factory.java.security;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.test.AbstractUtilsMavenTst;
import net.sf.ahtutils.xml.access.Category;
import net.sf.ahtutils.xml.access.View;
import net.sf.ahtutils.xml.access.Views;
import net.sf.ahtutils.xml.navigation.Navigation;
import net.sf.ahtutils.xml.navigation.UrlMapping;
import net.sf.ahtutils.xml.navigation.ViewPattern;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJavaSecurityFactoryTst extends AbstractUtilsMavenTst
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJavaSecurityFactoryTst.class);	
	
	protected static final String rootDir = "src/test/resources/data/factory/java/security";
	
	protected static File fTmpDir;
	
	protected List<Category> lC;
	protected Category c1;
	protected View v1,v2,v3;
	protected Navigation n1,n2,n3;
	
	@Before
	public void initViews()
	{
		n1 = new Navigation();
		n1.setUrlMapping(new UrlMapping());n1.getUrlMapping().setValue("url1");
		n1.setViewPattern(new ViewPattern());n1.getViewPattern().setValue("view1");
		v1 = new View();
		v1.setPublic(false);
		v1.setCode("myCode");
		v1.setNavigation(n1);
		v1.getNavigation().setPackage("my.package1");
		
		n2 = new Navigation();
		n2.setUrlMapping(new UrlMapping());n2.getUrlMapping().setValue("url2");
		n2.setViewPattern(new ViewPattern());n2.getViewPattern().setValue("view2");
		v2 = new View();
		v2.setPublic(false);
		v2.setCode("myCode2");
		v2.setNavigation(n2);
		v2.getNavigation().setPackage("my.package1");
		
		n3 = new Navigation();
		n3.setUrlMapping(new UrlMapping());n3.getUrlMapping().setValue("url2");
		n3.setViewPattern(new ViewPattern());n3.getViewPattern().setValue("view2");
		v3 = new View();
		v3.setPublic(false);
		v3.setCode("myCode3");
		v3.setNavigation(n3);
		v3.getNavigation().setPackage("my.package2");
		
		c1 = new Category();c1.setCode("xx");
		c1.setViews(new Views());
		c1.getViews().getView().add(v1);
		c1.getViews().getView().add(v2);
		c1.getViews().getView().add(v3);
		
		lC = new ArrayList<Category>();
		lC.add(c1);
	}
	
	@BeforeClass
	public static void initTmpDir()
	{
		fTmpDir = new File(fTarget,"securityTmp");
		if(!fTmpDir.exists()){fTmpDir.mkdir();}
	}
	
//	@AfterClass
	public static void rmTmpDir() throws IOException
	{
		FileUtils.deleteDirectory(fTmpDir);
	}
}