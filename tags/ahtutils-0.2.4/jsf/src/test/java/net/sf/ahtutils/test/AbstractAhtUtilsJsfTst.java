package net.sf.ahtutils.test;

import java.io.File;

import net.sf.ahtutils.xml.AhtUtilsNsPrefixMapper;
import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.xml.ns.NsPrefixMapperInterface;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAhtUtilsJsfTst extends AbstractAhtUtilsTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractAhtUtilsJsfTst.class);
	
	protected static NsPrefixMapperInterface nsPrefixMapper;
	protected static File fTarget;
	
	@BeforeClass
	public static void initFile()
	{
		if(!LoggerInit.isLog4jInited()){initLogger();}
		String dirTarget = System.getProperty("targetDir");
		if(dirTarget==null){dirTarget="target";}
		setfTarget(new File(dirTarget));
		logger.debug("Using targeDir "+fTarget.getAbsolutePath());
	}
	protected static void setfTarget(File fTarget) {AbstractAhtUtilsJsfTst.fTarget = fTarget;}
	
	@BeforeClass
    public static void initLogger()
	{
		if(!LoggerInit.isLog4jInited())
		{
			LoggerInit loggerInit = new LoggerInit("log4junit.xml");	
			loggerInit.addAltPath("config.ahtutils-jsf.test");
			loggerInit.init();
		}
    }
	
	protected NsPrefixMapperInterface getPrefixMapper()
	{
		if(nsPrefixMapper==null){nsPrefixMapper = new AhtUtilsNsPrefixMapper();}
		return nsPrefixMapper;
	}
}