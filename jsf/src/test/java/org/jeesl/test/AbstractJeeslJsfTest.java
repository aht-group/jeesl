package org.jeesl.test;

import java.io.File;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.interfaces.io.NsPrefixMapperInterface;
import org.exlp.util.io.log.LoggerInit;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractJeeslTest;

public class AbstractJeeslJsfTest extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslJsfTest.class);
	
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
	protected static void setfTarget(File fTarget) {AbstractJeeslJsfTest.fTarget = fTarget;}
	
	@BeforeAll
    public static void initLogger()
	{
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
    }
	
	protected NsPrefixMapperInterface getPrefixMapper()
	{
		if(nsPrefixMapper==null){nsPrefixMapper = new JeeslNsPrefixMapper();}
		return nsPrefixMapper;
	}
}