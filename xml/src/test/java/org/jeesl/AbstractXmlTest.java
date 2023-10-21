package org.jeesl;

import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractAhtUtilsXmlTest;
import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.xml.JaxbUtil;

public abstract class AbstractXmlTest <T extends Object> extends AbstractAhtUtilsXmlTest<T>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractXmlTest.class);
	
	public AbstractXmlTest(){this(null,null);}
	public AbstractXmlTest(Class<T> cXml,String xmlDirSuffix)
	{
		super(cXml,xmlDirSuffix);
	}
	
	@BeforeClass
    public static void initLogger()
	{
		LoggerInit loggerInit = new LoggerInit("log4junit.xml");	
		loggerInit.path("config.ahtutils-xml.test");
		loggerInit.init();
    }
	
	@BeforeClass
	public static void initJaxb()
	{
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
}