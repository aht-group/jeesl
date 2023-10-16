package org.jeesl;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.xml.ns.NsPrefixMapperInterface;

public class AbstractJeeslPrototypeTest 
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslPrototypeTest.class);
	
	protected static NsPrefixMapperInterface nsPrefixMapper;
	protected static File fTarget;
		
	@BeforeAll
    public static void initLogger()
	{
		if(!LoggerInit.isLog4jInited())
		{
			LoggerInit loggerInit = new LoggerInit("log4junit.xml");	
			loggerInit.addAltPath("jeesl/prototype/test");
			loggerInit.init();
		}
    }
	
	@Test public void log4j()
	{
		
	}
}