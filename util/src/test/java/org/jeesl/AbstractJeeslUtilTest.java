package org.jeesl;

import java.io.File;

import net.sf.ahtutils.test.AbstractJeeslTest;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJeeslUtilTest extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslUtilTest.class);
	
	protected File f;
	protected boolean saveReference=false;

	@BeforeAll
	public static void initFile()
	{
		if(!LoggerBootstrap.isLog4jInited()){initLogger();}
		AbstractJeeslTest.initTargetDirectory();
	}
	
	@BeforeAll
    public static void initLogger()
	{
		if(!LoggerBootstrap.isLog4jInited())
		{
			LoggerBootstrap.instance().path("jeesl/system/io/log").init();
		}
    }
	
	@BeforeAll
	public static void initPrefixMapper()
	{
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
	
	protected void assertJaxbEquals(Object expected, Object actual)
	{
		Assertions.assertEquals(JaxbUtil.toString(expected),JaxbUtil.toString(actual),"actual XML differes from expected XML");
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