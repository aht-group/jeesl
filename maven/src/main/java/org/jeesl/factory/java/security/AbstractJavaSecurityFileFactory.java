package org.jeesl.factory.java.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.factory.AbstractFreemarkerFileFactory;
import net.sf.ahtutils.xml.security.Category;
import net.sf.ahtutils.xml.security.Security;
import net.sf.exlp.util.xml.JaxbUtil;

public class AbstractJavaSecurityFileFactory extends AbstractFreemarkerFileFactory 
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJavaSecurityFileFactory.class);
		
	protected String classPrefix;
	
	public AbstractJavaSecurityFileFactory(File fTmpDir, String classPrefix)
	{
		super(fTmpDir);
		this.classPrefix=classPrefix;
	}
	
	protected String createFileName(String code)
	{
		return createClassName(code)+".java";
	}
	
	public void processViewsOld(String fXml) throws FileNotFoundException, UtilsConfigurationException
	{
		Security access = JaxbUtil.loadJAXB(fXml, Security.class);
		processCategoriesOld(access.getCategory());
	}
	public void processViews(String fXml) throws FileNotFoundException, UtilsConfigurationException
	{
		Security security = JaxbUtil.loadJAXB(fXml, Security.class);
		processCategories(security.getCategory());
	}
	
	protected void processCategoriesOld(List<Category> lCategory) throws UtilsConfigurationException {logger.warn("This needs to be overridden");}
	protected void processCategories(List<net.sf.ahtutils.xml.security.Category> lCategory) throws UtilsConfigurationException {logger.warn("This needs to be overridden");}
	
	protected String createClassName(String code)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(classPrefix);
		sb.append(code.subSequence(0, 1).toString().toUpperCase());
		sb.append(code.substring(1, code.length()));
		return sb.toString();
	}
	
	protected static String buildPackage(String code)
	{
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<code.length();i++)
		{
		    char c = code.charAt(i);
		    if(Character.isUpperCase(c)){sb.append(".").append(Character.toLowerCase(c));}
		    else{sb.append(c);}
		}
		return sb.toString();
	}
	
	protected static String buildPackageFile(String code)
	{
		String dotPackage = buildPackage(code);
		return dotPackage.replace(".",File.separator);
	}
}