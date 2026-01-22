package org.jeesl.controller.handler.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.LogManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.Configuration;
import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.controller.handler.system.property.ConfigLoader;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.exlp.util.io.log.LoggerInit;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.handler.system.property.ConfigBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

public class JeeslCliOptionHandler
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCliOptionHandler.class);
	
	private Options options;
	private Option oHelp,oDebug,oConfig;
	private Option oLogFile;
	
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
	
	public void buildLogFile()
	{
		oLogFile = new Option("logFile", "Log to File");
		options.addOption(oLogFile);
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

	public JeeslCliOptionHandler setLogPaths(String... paths)
	{
		log4jPaths = paths;
		return this;
	}
	
	public void handleLogJul(CommandLine line)
	{
		if(line.hasOption(oDebug.getOpt())) {this.initLogger("debug.jul.properties");}
        else {this.initLogJul("app.jul.properties");}
	}
	private void initLogJul(String loggingProfile)
	{
		for(String path : log4jPaths)
		{
			try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(path+"/"+loggingProfile))
			{
		          LogManager.getLogManager().readConfiguration(is);
			}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public void handleLog4j1(CommandLine line)
	{
		if(line.hasOption(oDebug.getOpt())) {this.initLogger("debug.log4j.xml");}
        else {this.initLogger("app.log4j.xml");}
	}
	private void initLogger(String loggingProfile)
	{
		LoggerInit loggerInit = LoggerInit.instance(loggingProfile);
		for(String path : log4jPaths) {loggerInit.path(path);}
		loggerInit.setAllLoadTypes(LoggerInit.LoadType.File,LoggerInit.LoadType.Resource);
		loggerInit.init();
	}
	
	public void handleLog4j2(CommandLine line)
	{
		if(Objects.nonNull(oLogFile) && line.hasOption(oLogFile.getOpt())) {this.initLogger2("file.log4j2.xml");}
		else if(line.hasOption(oDebug.getOpt())) {this.initLogger2("debug.log4j2.xml");}
        else {this.initLogger2("app.log4j2.xml");}
	}
	private void initLogger2(String loggingProfile)
	{
//		System.out.println(log4jPaths[0]+"/"+loggingProfile);
		LoggerBootstrap.instance(loggingProfile).path(log4jPaths[0]).init();
//		LoggerBootstrap.instance().path(log4jPaths[0]).init();
		
//		System.out.println("Log4j2 SUCCESS");
//		logger.error("Log4j2 initialized (on error)");
//		logger.warn("Log4j2 initialized (on warn)");
//		logger.info("Log4j2 initialized (on info)");
	}
	
	private org.exlp.interfaces.system.property.Configuration config1Wrapper(CommandLine line, String defaultConfig)
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
			
			MultiResourceLoader mrl = MultiResourceLoader.instance();
			if(!mrl.isAvailable(configFile))
			{
				logger.error("Specified configuration does not exist: "+configFile);
				System.exit(-1);
			}
			logger.info("Using "+Configuration.class.getSimpleName()+" "+configFile);
			ConfigLoader.addString(configFile);
	    }
		
		ConfigLoader.addString(defaultConfig);
		
		return ConfigLoader.wrap(ConfigLoader.init());
	}
	
	public org.exlp.interfaces.system.property.Configuration config2Wrapper(CommandLine line, String defaultConfig) {return ConfigBootstrap.wrap(config2(line,defaultConfig));}
	private org.apache.commons.configuration2.Configuration config2(CommandLine line, String defaultConfig)
	{
		ConfigBootstrap bootstrap = ConfigBootstrap.instance();
		if(line.hasOption(oConfig.getOpt()))
		{
			String configFile = line.getOptionValue(oConfig.getOpt());
			
			if(configFile.equals("exlp"))
			{
				logger.info("Using "+ExlpCentralConfigPointer.class.getSimpleName());
				try
				{
					ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(exlpApp).jaxb(JaxbUtil.instance());
					bootstrap.add(ccp.toFile(exlpCode).toPath());
				}
				catch (ExlpConfigurationException e)
				{
					logger.error(e.getMessage());
					System.exit(-1);
				}
			}
			
			MultiResourceLoader mrl = MultiResourceLoader.instance();
			if(!mrl.isAvailable(configFile))
			{
				logger.error("Specified configuration does not exist: "+configFile);
				System.exit(-1);
			}
			logger.info("Using "+Configuration.class.getSimpleName()+" "+configFile);
			bootstrap.add(configFile);
	    }
		
		bootstrap.add(defaultConfig);
		
		return bootstrap.combine();
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
			
			MultiResourceLoader mrl = MultiResourceLoader.instance();
			if(!mrl.isAvailable(configFile))
			{
				logger.error("Specified configuration does not exist: "+configFile);
				System.exit(-1);
			}
			logger.info("Using "+Configuration.class.getSimpleName()+" "+configFile);
			cl.add(configFile);
	    }
		
		cl.add(defaultConfig);
		
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