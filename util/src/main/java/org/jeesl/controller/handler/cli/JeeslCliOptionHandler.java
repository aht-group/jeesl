package org.jeesl.controller.handler.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.logging.LogManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.Configuration;
import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.controller.handler.system.property.ConfigLoader;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.handler.system.property.ConfigBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

/**
 * Reusable help, logging and configuration handling for command-line entry points that parse their
 * arguments with Apache Commons CLI.
 */
public class JeeslCliOptionHandler
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCliOptionHandler.class);
	
	private Options options;
	
	/**
	 * Returns the option set that an entry point augments with its own options.
	 * @return the option set
	 */
	public Options getOptions() {return options;}
	
	private Option oHelp,oDebug;
	private Option oLogFile;
	
	private Option oConfig;
	
	/**
	 * Returns the option that selects an individual configuration file.
	 * @return the config option
	 */
	public Option getConfigOption() {return oConfig;}

	private IntConsumer exitHandler = System::exit; void setExitHandler(IntConsumer exitHandler) {this.exitHandler = exitHandler;}

	private boolean appStarted;
	
	/**
	 * Indicates whether the consuming application may start.
	 * @return true once a caller has been allowed to start
	 */
	public boolean isAppStarted(){return appStarted;}
	
	private String version;
	private String[] log4jPaths;
	
	private String exlpApp;
	
	/**
	 * Returns the application code used to resolve the central EXLP configuration.
	 * @return the EXLP application code
	 */
	public String getExlpApp() {return exlpApp;}
	
	/**
	 * Sets the application code used to resolve the central EXLP configuration.
	 * @param exlpApp the EXLP application code
	 */
	public void setExlpApp(String exlpApp) {this.exlpApp = exlpApp;}
	
	private String exlpCode;
	
	/**
	 * Returns the configuration code used to resolve the central EXLP configuration.
	 * @return the EXLP configuration code
	 */
	public String getExlpCode() {return exlpCode;}
	
	/**
	 * Sets the configuration code used to resolve the central EXLP configuration.
	 * @param exlpCode the EXLP configuration code
	 */
	public void setExlpCode(String exlpCode) {this.exlpCode = exlpCode;}

	/**
	 * Creates the handler and an empty option set.
	 * @param version the version string shown in the help text
	 */
	public JeeslCliOptionHandler(String version)
	{
		this.version = version;
		appStarted = false;
		options = new Options();
		
		exlpApp = "unknown";
		exlpCode = "unknown";
	}
	
	/**
	 * Registers the option that prints the help text.
	 */
	public void buildHelp()
	{
		oHelp = new Option("help", "Prints this message");
		options.addOption(oHelp);
	}
	
	/**
	 * Registers the option that selects the debug logging profile.
	 */
	public void buildDebug()
	{
		oDebug = new Option("debug", "Debug output");
		options.addOption(oDebug);
	}
	
	/**
	 * Registers the option that selects the file logging profile.
	 */
	public void buildLogFile()
	{
		oLogFile = new Option("logFile", "Log to File");
		options.addOption(oLogFile);
	}
	
	/**
	 * Registers the optional option that selects an individual configuration file.
	 */
	public void buildConfig()
	{
		oConfig = Option.builder("config").required(false).hasArg(true).argName("FILE").desc("Use individual configuration FILE").build(); 
		options.addOption(oConfig);
	}
	
	/**
	 * Prints the help text and ends the process with exit code 0 when the command line carries the help option.
	 * @param line the parsed command line
	 */
	public void handleHelp(CommandLine line)
	{
		if(line.hasOption(oHelp.getOpt())) {help();}
	}
	
	/**
	 * Prints the option help text and ends the process with exit code 0.
	 */
	public void help()
	{    	
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "java -jar xxx"+version, options );
		exitHandler.accept(0);
	}

	/**
	 * Selects the Log4j2 profile from the log file option, the debug option or the default.
	 * @param line the parsed command line
	 * @return the file name of the selected profile
	 */
	public String loggingProfile(CommandLine line)
	{
		if(Objects.nonNull(oLogFile) && line.hasOption(oLogFile.getOpt())) {return "file.log4j2.xml";}
		else if(line.hasOption(oDebug.getOpt())) {return "debug.log4j2.xml";}
		else {return "app.log4j2.xml";}
	}

	/**
	 * Sets the resource paths searched for the Log4j2 configuration files.
	 * @param paths the candidate resource paths
	 * @return this handler
	 */
	public JeeslCliOptionHandler setLogPaths(String... paths)
	{
		log4jPaths = paths;
		return this;
	}
	
	
	/**
	 * Initialises Log4j2 with the profile selected from the command line.
	 * @param line the parsed command line
	 */
	public void handleLog4j2(CommandLine line)
	{
		this.initLogger2(this.loggingProfile(line));
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
					exitHandler.accept(-1);
				}
			}
			
			MultiResourceLoader mrl = MultiResourceLoader.instance();
			if(!mrl.isAvailable(configFile))
			{
				logger.error("Specified configuration does not exist: "+configFile);
				exitHandler.accept(-1);
			}
			logger.info("Using "+Configuration.class.getSimpleName()+" "+configFile);
			ConfigLoader.addString(configFile);
	    }
		
		ConfigLoader.addString(defaultConfig);
		
		return ConfigLoader.wrap(ConfigLoader.init());
	}
	
	/**
	 * Reads the configuration selected by the config option and adds the default configuration.
	 * @param line the parsed command line
	 * @param defaultConfig the configuration that is always added
	 * @return the combined configuration
	 */
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
					configFile = ccp.toFile(exlpCode).getAbsolutePath();
				}
				catch (ExlpConfigurationException e)
				{
					logger.error(e.getMessage());
					exitHandler.accept(-1);
				}
			}
			
			MultiResourceLoader mrl = MultiResourceLoader.instance();
			if(!mrl.isAvailable(configFile))
			{
				logger.error("Specified configuration does not exist: "+configFile);
				exitHandler.accept(-1);
			}
			logger.info("Using "+Configuration.class.getSimpleName()+" "+configFile);
			bootstrap.add(configFile);
	    }
		
		bootstrap.add(defaultConfig);
		
		return bootstrap.combine();
	}
	
	/**
	 * Reads the configuration selected by the config option and adds the default configuration.
	 * @param line the parsed command line
	 * @param defaultConfig the configuration that is always added
	 * @return the combined configuration
	 */
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
					exitHandler.accept(-1);
				}
			}
			
			MultiResourceLoader mrl = MultiResourceLoader.instance();
			if(!mrl.isAvailable(configFile))
			{
				logger.error("Specified configuration does not exist: "+configFile);
				exitHandler.accept(-1);
			}
			logger.info("Using "+Configuration.class.getSimpleName()+" "+configFile);
			cl.add(configFile);
	    }
		
		cl.add(defaultConfig);
		
		return cl.combine();
	}
	
	/**
	 * Allows the consuming application to start once.
	 * @return true on the first call and on every later call
	 */
	public boolean allowAppStart()
	{
		if(!appStarted){appStarted = true;}
		return appStarted;
	}
}