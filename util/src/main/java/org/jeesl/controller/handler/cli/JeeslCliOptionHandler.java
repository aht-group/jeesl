package org.jeesl.controller.handler.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.Configuration;
import org.jeesl.controller.handler.system.property.ConfigBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.config.ConfigLoader;
import net.sf.exlp.util.io.ExlpCentralConfigPointer;
import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import net.sf.exlp.util.xml.JaxbUtil;

public class JeeslCliOptionHandler
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCliOptionHandler.class);
	
	private Options options;
	private Option oHelp,oDebug,oConfig;
	
	private boolean appStarted;
	public boolean isAppStarted(){return appStarted;}
	
	private String version;
	private String[] log4jPaths;
	
	private String exlpApp; public String getExlpApp() {return exlpApp;}
	private String exlpCode; public String getExlpCode() {return exlpCode;}

	public JeeslCliOptionHandler(String version)
	{
		this.version = version;
		appStarted = false;
		options = new Options();
		
		exlpApp = "unknown";
		exlpCode = "unknown";
	}
	
	public void buildHelp()
	{
		oHelp = new Option("help", "Prints this message");
		options.addOption(oHelp);
	}
	
	public void buildDebug()
	{
		oDebug = new Option("debug", "Debug output");
		options.addOption(oDebug);
	}
	
	public void buildConfig()
	{
		oConfig = Option.builder("config").required(false).hasArg(true).argName("FILE").desc("Use individual configuration FILE").build(); 
		options.addOption(oConfig);
	}
	
	public void handleHelp(CommandLine line)
	{
		if(line.hasOption(oHelp.getOpt())) {help();}
	}
	
	public void help()
	{    	
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "java -jar xxx"+version, options );
		System.exit(0);
	}
	
	// *** DEBUG ***
	public void setLog4jPaths(String... paths)
	{
		log4jPaths = paths;
	}
	
	public void handleLogger(String alternativePath, CommandLine line)
	{
		this.setLog4jPaths(alternativePath);
		if(line.hasOption(oDebug.getOpt())) {this.initLogger("log4j.debug.xml");}
        else {this.initLogger("log4j.cli.xml");}
	}
	public void handleLogger(CommandLine line)
	{
		if(line.hasOption(oDebug.getOpt())) {this.initLogger("log4j.debug.xml");}
        else {this.initLogger("log4j.cli.xml");}
	}
	
	private void initLogger(String loggingProfile)
	{
		LoggerInit loggerInit = new LoggerInit(loggingProfile);
		for(String path : log4jPaths){loggerInit.addAltPath(path);}
		loggerInit.setAllLoadTypes(LoggerInit.LoadType.File,LoggerInit.LoadType.Resource);
		loggerInit.init();
	}
	
	// *** CONFIG ***
	public org.apache.commons.configuration.Configuration initConfig(CommandLine line, String defaultConfig)
	{
		if(line.hasOption(oConfig.getOpt()))
		{
			String configFile = line.getOptionValue(oConfig.getOpt());
			
			if(configFile.equals("exlp"))
			{
				logger.info("Using "+ExlpCentralConfigPointer.class.getSimpleName());
				try
				{
					ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(exlpApp).jaxb(JaxbUtil.instance());
					configFile = ccp.toFile(exlpCode).getAbsolutePath();
				}
				catch (ExlpConfigurationException e)
				{
					logger.error(e.getMessage());
					System.exit(-1);
				}
			}
			
			MultiResourceLoader mrl = new MultiResourceLoader();
			if(!mrl.isAvailable(configFile))
			{
				logger.error("Specified configuration does not exist: "+configFile);
				System.exit(-1);
			}
			logger.info("Using "+Configuration.class.getSimpleName()+" "+configFile);
			ConfigLoader.add(configFile);
	    }
		
		ConfigLoader.add(defaultConfig);
		
		return ConfigLoader.init();
	}
	
	public org.apache.commons.configuration2.Configuration toConfig(CommandLine line, String defaultConfig)
	{
		ConfigBootstrap cl = ConfigBootstrap.instance();
		if(line.hasOption(oConfig.getOpt()))
		{
			String configFile = line.getOptionValue(oConfig.getOpt());
			
			if(configFile.equals("exlp"))
			{
				logger.info("Using "+ExlpCentralConfigPointer.class.getSimpleName());
				try
				{
					ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(exlpApp).jaxb(JaxbUtil.instance());
					configFile = ccp.toFile(exlpCode).getAbsolutePath();
				}
				catch (ExlpConfigurationException e)
				{
					logger.error(e.getMessage());
					System.exit(-1);
				}
			}
			
			MultiResourceLoader mrl = new MultiResourceLoader();
			if(!mrl.isAvailable(configFile))
			{
				logger.error("Specified configuration does not exist: "+configFile);
				System.exit(-1);
			}
			logger.info("Using "+Configuration.class.getSimpleName()+" "+configFile);
			cl.addS(configFile);
	    }
		
		cl.addS(defaultConfig);
		
		return cl.combine();
	}
	
	public boolean allowAppStart()
	{
		if(!appStarted){appStarted = true;}
		return appStarted;
	}
	
	public Options getOptions() {return options;}
	
	public void setExlpApp(String exlpApp) {this.exlpApp = exlpApp;}
	public void setExlpCode(String exlpCode) {this.exlpCode = exlpCode;}
}