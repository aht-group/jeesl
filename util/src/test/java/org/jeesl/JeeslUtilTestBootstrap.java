package org.jeesl;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.exlp.controller.handler.system.property.ConfigLoader;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractJeeslTest;
import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.ExlpCentralConfigPointer;
import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.xml.JaxbUtil;

public class JeeslUtilTestBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslUtilTestBootstrap.class);
	
	public enum App {jeesl}
	
	public static Configuration init()
	{
		AbstractJeeslTest.setTargetDirectory(new File("target"));
		
		LoggerInit loggerInit = new LoggerInit("log4j.xml");
		loggerInit.path("jeesl/util/config");
		loggerInit.init();
		
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
		
		try
		{
			ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(App.jeesl).jaxb(JaxbUtil.instance());
			ConfigLoader.addFile(ccp.toFile("util"));
		}
		catch (ExlpConfigurationException e) {logger.debug("No additional "+ExlpCentralConfigPointer.class.getSimpleName()+" because "+e.getMessage());}

		Configuration config = ConfigLoader.init();
		
		logger.debug("Config and Logger initialized");
		return config;
	}
}