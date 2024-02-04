package net.sf.ahtutils.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.exlp.util.io.StringUtil;
import org.exlp.util.io.log.LoggerInit;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractUtilsMavenTst
{
	final static Logger logger = LoggerFactory.getLogger(AbstractUtilsMavenTst.class);
	
	protected File f;
	protected boolean saveReference=false;

	protected static File fTarget;

	@BeforeAll
	public static void initFile()
	{
		if(!LoggerInit.isLog4jInited()){initLogger();}
		String dirTarget = System.getProperty("targetDir");
		if(dirTarget==null){dirTarget="target";}
		
		logger.debug("Using targeDir "+fTarget.getAbsolutePath());
	}
	
	@BeforeAll
    public static void initLogger()
	{
		if(!LoggerInit.isLog4jInited())
		{
			LoggerInit loggerInit = new LoggerInit("log4junit.xml");	
			loggerInit.path("config.ahtutils-maven.test");
			loggerInit.init();
		}
    }
	
	@BeforeAll
	public static void initPrefixMapper()
	{
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
	
	protected void assertJaxbEquals(Object expected, Object actual)
	{
		Assertions.assertEquals("actual XML differes from expected XML",JaxbUtil.toString(expected),JaxbUtil.toString(actual));
	}
	
	protected void saveXml(Object xml, File f, boolean formatted)
	{
		if(saveReference)
		{
			logger.debug("Saving Reference XML");
			JaxbUtil.debug(xml);
			JaxbUtil.save(f, xml, formatted);
		}
	}
	
	protected void assertText(File fExpected, File fActual) throws IOException
	{
		if(saveReference)
		{
			FileUtils.copyFile(fActual, fExpected);
			logger.info("*********************");
			System.out.println(StringUtil.readFile(fActual));
		}
		
		String expected = StringUtil.readFile(fExpected);
		String actual = StringUtil.readFile(fActual);
		Assertions.assertEquals("Texts are different",expected, actual);
	}
	
	public void setSaveReference(boolean saveReference) {this.saveReference = saveReference;}
}