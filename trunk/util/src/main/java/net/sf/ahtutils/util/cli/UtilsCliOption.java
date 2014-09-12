package net.sf.ahtutils.util.cli;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.config.ConfigLoader;
import net.sf.exlp.util.io.ExlpCentralConfigPointer;
import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsCliOption
{
	final static Logger logger = LoggerFactory.getLogger(UtilsCliOption.class);
	
	private Options options;
	private Option oHelp,oDebug,oConfig;
	
	private boolean appStarted;
	public boolean isAppStarted(){return appStarted;}
	
	private String version;
	private String[] log4jPaths;
	
	private String exlpApp,exlpCode;

	public UtilsCliOption(String version)
	{
		this.version=version;
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
	
	@SuppressWarnings("static-access")
	public void buildConfig()
	{
		oConfig = OptionBuilder.withArgName("FILE").hasArg().withDescription( "Use individual configuration FILE").create("config");
		options.addOption(oConfig);
	}
	
	// *** HELP ***
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
	
	public void handleLogger(CommandLine line)
	{
		if(line.hasOption(oDebug.getOpt())) {initLogger("log4j.debug.xml");}
        else
        {
//        	System.out.println("Using cli");
        	initLogger("log4j.cli.xml");
        }
	}
	
	private void initLogger(String logConfig)
	{
		LoggerInit loggerInit = new LoggerInit(logConfig);
		for(String path : log4jPaths){loggerInit.addAltPath(path);}
		loggerInit.setAllLoadTypes(LoggerInit.LoadType.File,LoggerInit.LoadType.Resource);
		loggerInit.init();
	}
	
	// *** CONFIG ***
	public Configuration initConfig(CommandLine line, String defaultConfig)
	{
		if(line.hasOption(oConfig.getOpt()))
		{
			String configFile = line.getOptionValue(oConfig.getOpt());
			
			if(configFile.equals("exlp"))
			{
				logger.info("Using "+ExlpCentralConfigPointer.class.getSimpleName());
				try
				{
					configFile = ExlpCentralConfigPointer.getFile(exlpApp,exlpCode).getAbsolutePath();
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
			ConfigLoader.add(configFile);
	    }
		
		ConfigLoader.add(defaultConfig);
		
		return ConfigLoader.init();
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
