package org.jeesl.factory.java.security;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.TemplateException;
import net.sf.ahtutils.xml.security.View;
import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.dir.DirChecker;
import net.sf.exlp.util.io.dir.RecursiveFileFinder;
import net.sf.exlp.util.xml.JaxbUtil;

public class JavaSecurityViewIdentifierFactory extends AbstractJavaSecurityFileFactory
{
	final static Logger logger = LoggerFactory.getLogger(JavaSecurityViewIdentifierFactory.class);
		
	private File fPackage;
	private String viewQualifierBasePackage;
	
	
	public JavaSecurityViewIdentifierFactory(File fTmpDir, File fPackage, String viewQualifierBasePackage, String classPrefix)
	{
		super(fTmpDir,classPrefix);
		this.fPackage=fPackage;
		this.viewQualifierBasePackage=viewQualifierBasePackage;
	}
	
	
	@Override protected void processCategories(List<net.sf.ahtutils.xml.security.Category> categories) throws UtilsConfigurationException
	{
		try{DirChecker.checkFileIsDirectory(fPackage);}
		catch (ExlpConfigurationException e) {throw new UtilsConfigurationException(e.getMessage());}
		
		Set<String> setExistingFiles = new HashSet<String>();
		List<String> listCreatedFiles = new ArrayList<String>();
		
		try
		{
			RecursiveFileFinder finder = new RecursiveFileFinder(FileFilterUtils.suffixFileFilter(".java"));
			for(File f : finder.find(fPackage))
			{
				logger.debug("Finder: "+f.getAbsolutePath());
				setExistingFiles.add(f.getAbsolutePath());
			}
		}
		catch (IOException e) {e.printStackTrace();}
		
		for(net.sf.ahtutils.xml.security.Category category : categories)
		{
			try {listCreatedFiles.addAll(create(category));}
			catch (IOException e) {e.printStackTrace();}
			catch (TemplateException e) {e.printStackTrace();}
		}
		
		for(String s : listCreatedFiles){setExistingFiles.remove(s);}
		if(setExistingFiles.size()>0)
		{
			logger.warn("Deleting unused Identifier");
			Iterator<String> iterator = setExistingFiles.iterator();
			while(iterator.hasNext())
			{
				String key = iterator.next();
				File f = new File(key);
				f.delete();
				logger.warn("\t"+key);
			}
			
		}
	}
	
	protected List<String> create(net.sf.ahtutils.xml.security.Category category) throws UtilsConfigurationException, IOException, TemplateException
	{
		List<String> created = new ArrayList<String>();
		File fCategory = new File(fPackage,buildPackageFile(category.getCode()));
		logger.debug("Checking directory: "+fCategory);
		if(fCategory.exists())
		{
			if(fCategory.isDirectory()){logger.debug("Skipping. Directory exists "+fCategory.getAbsolutePath());}
			else{throw new UtilsConfigurationException(fCategory.getAbsolutePath()+" is not a directory");}
		}
		else
		{
			fCategory.mkdir();
			logger.debug("Create directory "+fCategory.getAbsolutePath());
		}
		
		if(category.isSetViews())
		{
			for(net.sf.ahtutils.xml.security.View view : category.getTmp().getView())
			{
				File fProcessed = createIdentifier(fCategory,view,buildPackage(category.getCode()));
				created.add(fProcessed.getAbsolutePath());
			}
		}
		return created;
	}

	@Deprecated @Override protected void processCategoriesOld(List<net.sf.ahtutils.xml.access.Category> categories) throws UtilsConfigurationException
	{
		try{DirChecker.checkFileIsDirectory(fPackage);}
		catch (ExlpConfigurationException e) {throw new UtilsConfigurationException(e.getMessage());}
		
		Set<String> setExistingFiles = new HashSet<String>();
		List<String> listCreatedFiles = new ArrayList<String>();
		
		try
		{
			RecursiveFileFinder finder = new RecursiveFileFinder(FileFilterUtils.suffixFileFilter(".java"));
			for(File f : finder.find(fPackage))
			{
				logger.debug("Finder: "+f.getAbsolutePath());
				setExistingFiles.add(f.getAbsolutePath());
			}
		}
		catch (IOException e) {e.printStackTrace();}
		
		for(net.sf.ahtutils.xml.access.Category category : categories)
		{
			try {listCreatedFiles.addAll(create(category));}
			catch (IOException e) {e.printStackTrace();}
			catch (TemplateException e) {e.printStackTrace();}
		}
		
		for(String s : listCreatedFiles){setExistingFiles.remove(s);}
		if(setExistingFiles.size()>0)
		{
			logger.warn("Deleting unused Identifier");
			Iterator<String> iterator = setExistingFiles.iterator();
			while(iterator.hasNext())
			{
				String key = iterator.next();
				File f = new File(key);
				f.delete();
				logger.warn("\t"+key);
			}
			
		}
	}

	@Deprecated protected List<String> create(net.sf.ahtutils.xml.access.Category category) throws UtilsConfigurationException, IOException, TemplateException
	{
		List<String> created = new ArrayList<String>();
		File fCategory = new File(fPackage,buildPackageFile(category.getCode()));
		logger.debug("Checking directory: "+fCategory);
		if(fCategory.exists())
		{
			if(fCategory.isDirectory()){logger.debug("Skipping. Directory exists "+fCategory.getAbsolutePath());}
			else{throw new UtilsConfigurationException(fCategory.getAbsolutePath()+" is not a directory");}
		}
		else
		{
			fCategory.mkdir();
			logger.debug("Create directory "+fCategory.getAbsolutePath());
		}
		
		if(category.isSetViews())
		{
			for(View view : category.getViews().getView())
			{
				File fProcessed = createIdentifier(fCategory,view,buildPackage(category.getCode()));
				created.add(fProcessed.getAbsolutePath());
			}
		}
		return created;
	}
	
	@Deprecated private File createIdentifier(File fSub, View view,String subPackage) throws IOException, TemplateException
	{	
		freemarkerNodeModel.clear();
		freemarkerNodeModel.put("packageName", viewQualifierBasePackage+"."+buildPackage(subPackage));
		freemarkerNodeModel.put("className", createClassName(view.getCode()));
		
		File fJava = new File(fSub,createFileName(view.getCode()));
		fJava.createNewFile();
		
		logger.debug("Will create file: "+fJava.getAbsolutePath());
		
		this.createFile(fJava, "jeesl/freemarker/java/security/identifier.ftl");
		return fJava;
	}
}