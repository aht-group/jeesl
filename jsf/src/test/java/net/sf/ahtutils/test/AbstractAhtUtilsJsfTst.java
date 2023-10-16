package net.sf.ahtutils.test;

import java.io.File;

import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.xml.ns.NsPrefixMapperInterface;

public class AbstractAhtUtilsJsfTst extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractAhtUtilsJsfTst.class);
	
	protected static NsPrefixMapperInterface nsPrefixMapper;
	protected static File fTarget;
	
	@BeforeAll
	public static void initFile()
	{
		if(!LoggerInit.isLog4jInited()){initLogger();}
		String dirTarget = System.getProperty("targetDir");
		if(dirTarget==null){dirTarget="target";}
		setfTarget(new File(dirTarget));
		logger.debug("Using targeDir "+fTarget.getAbsolutePath());
	}
	protected static void setfTarget(File fTarget) {AbstractAhtUtilsJsfTst.fTarget = fTarget;}
	
	@BeforeAll
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
		if(nsPrefixMapper==null){nsPrefixMapper = new JeeslNsPrefixMapper();}
		return nsPrefixMapper;
	}
}