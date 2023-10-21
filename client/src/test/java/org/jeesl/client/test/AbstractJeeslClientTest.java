package org.jeesl.client.test;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractJeeslTest;
import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.xml.JaxbUtil;

public class AbstractJeeslClientTest extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslClientTest.class);
	
	protected File f;
	protected boolean saveReference=false;

	@BeforeAll
	public static void initFile()
	{
		if(!LoggerInit.isLog4jInited()){initLogger();}
		AbstractJeeslTest.initTargetDirectory();
	}
	
	@BeforeAll
    public static void initLogger()
	{
		if(!LoggerInit.isLog4jInited())
		{
			LoggerInit loggerInit = new LoggerInit("log4junit.xml");	
			loggerInit.path("jeesl/client/config");
			loggerInit.init();
		}
    }
	
	@BeforeAll
	public static void initPrefixMapper()
	{
//		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
	
	@Test public void dummy() {}
	
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
	
	public void setSaveReference(boolean saveReference) {this.saveReference = saveReference;}
}