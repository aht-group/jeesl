package org.jeesl.client.app;

import org.jeesl.client.web.rest.JeeslDbBackupNotifier;
import org.jeesl.client.web.rest.JeeslFontTrackerApp;
import org.jeesl.controller.handler.cli.JeeslCliOptionHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJeeslCliEntryPoints
{
	final static Logger logger = LoggerFactory.getLogger(TestJeeslCliEntryPoints.class);
	
	@Test
	public void clientModuleBuildsOptionsThroughHandler()
	{
		JeeslCliOptionHandler jco = new JeeslCliOptionHandler("0.0.0-test");
		jco.buildHelp();
		
		Assertions.assertTrue(jco.getOptions().hasOption("help"),"the option set contains an option named help");
	}
	
	@Test
	public void entryPointsUseHandler() throws Exception
	{
		Assertions.assertEquals(JeeslCliOptionHandler.class,
			JeeslMailSpooler.class.getMethod("parseArguments",JeeslCliOptionHandler.class,String[].class).getParameterTypes()[0],
			"JeeslMailSpooler uses the handler");
		Assertions.assertEquals(JeeslCliOptionHandler.class,
			JeeslDbBackupNotifier.class.getMethod("parseArguments",JeeslCliOptionHandler.class,String[].class).getParameterTypes()[0],
			"JeeslDbBackupNotifier uses the handler");
		Assertions.assertEquals(JeeslCliOptionHandler.class,
			JeeslFontTrackerApp.class.getMethod("parseArguments",JeeslCliOptionHandler.class,String[].class).getParameterTypes()[0],
			"JeeslFontTrackerApp uses the handler");
	}
}
