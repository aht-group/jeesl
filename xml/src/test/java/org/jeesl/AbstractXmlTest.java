package org.jeesl;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractAhtUtilsXmlTest;

public abstract class AbstractXmlTest <T extends Object> extends AbstractAhtUtilsXmlTest<T>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractXmlTest.class);
	
	public AbstractXmlTest(){this(null,null);}
	public AbstractXmlTest(Class<T> cXml,String xmlDirSuffix)
	{
		super(cXml,xmlDirSuffix);
	}
	
	@BeforeAll
    public static void initLogger()
	{
		LoggerBootstrap.instance("test.log4j2.xml").path("jeesl/system/io/log").init();
    }
	
	@BeforeAll
	public static void initJaxb()
	{
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
}