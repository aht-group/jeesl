package org.jeesl.client.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.Configuration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jeesl.api.rest.rs.jx.io.db.JeeslIoDbRest;
import org.jeesl.client.JeeslBootstrap;
import org.jeesl.controller.handler.cli.JeeslCliOptionHandler;
import org.jeesl.controller.processor.io.db.DatabaseBackupProcessor;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.exception.ExlpUnsupportedOsException;
import net.sf.exlp.interfaces.util.ConfigKey;

public class JeeslDbBackupNotifier
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDbBackupNotifier.class);
	
	private JeeslCliOptionHandler jco;
	private Option oUrl,oDirectory,oHost,oSystem;

	private String cfgUrl,cfgHost,cfgSystem;
	private File cfgDirectory;
	
	public JeeslDbBackupNotifier()
	{
		
	}
	
	private JeeslIoDbRest buildRest(String url)
	{
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget restTarget = client.target(url);
//		return restTarget.proxy(JeeslIoDbRest.class);
		return null;
	}
	
	public void local()
	{	
		Configuration config = JeeslBootstrap.init();
		
		cfgUrl = config.getString(ConfigKey.netRestUrlLocal);
		cfgDirectory = new File(config.getString("dir.db.backup"));
		cfgHost = "x";
		cfgSystem = "jeesl";
		
		debugConfig();
		DatabaseBackupProcessor processor = new DatabaseBackupProcessor(buildRest(cfgUrl),cfgDirectory,cfgHost,cfgSystem);
		processor.upload();
	}
	
	private void createOptions()
	{
		jco.buildHelp();
		jco.buildDebug();
        
        oUrl = Option.builder("url").required(true).hasArg(true).argName("URL").desc("URL Endpoint").build(); jco.getOptions().addOption(oUrl);
        oDirectory = Option.builder("dir").required(true).hasArg(true).argName("DIR").desc("Directory with .sql files").build(); jco.getOptions().addOption(oDirectory);
        oHost = Option.builder("host").required(true).hasArg(true).argName("HOST").desc("Host identifier of storage server").build(); jco.getOptions().addOption(oHost);
        oSystem = Option.builder("system").required(true).hasArg(true).argName("SYSTEM").desc("System identifier").build(); jco.getOptions().addOption(oSystem);
	}
	
	private void debugConfig()
	{
		logger.info("URL: "+cfgUrl);
		logger.info("Directory: "+cfgDirectory.getAbsolutePath());
		logger.info("Host: "+cfgHost);
		logger.info("System: "+cfgSystem);
	}
	
	public void parseArguments(JeeslCliOptionHandler jco, String args[]) throws Exception
	{
		this.jco = jco;
		
		createOptions();
		
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(jco.getOptions() , args); 
	     
		jco.handleHelp(line);
		jco.handleLog4j(line);
		
//		Configuration config = uOption.initConfig(line, MeisBootstrap.xmlConfig);
	    
		cfgUrl = line.getOptionValue(oUrl.getOpt());
		cfgDirectory = new File(line.getOptionValue(oDirectory.getOpt()));
		cfgHost = line.getOptionValue(oHost.getOpt());
		cfgSystem = line.getOptionValue(oSystem.getOpt());
		
		debugConfig();
		DatabaseBackupProcessor processor = new DatabaseBackupProcessor(buildRest(cfgUrl),cfgDirectory,cfgHost,cfgSystem);
		processor.upload();
	}
	
	public static void main(String args[]) throws FileNotFoundException, UtilsConfigurationException, NamingException, ExlpConfigurationException
	{
		JeeslDbBackupNotifier notifier = new JeeslDbBackupNotifier();
		
		notifier.local(); System.exit(0);
		
		JeeslCliOptionHandler jco = new JeeslCliOptionHandler(org.jeesl.Version.class.getPackage().getImplementationVersion());
		jco.setLog4jPaths("jeesl/client/config");
		
		try {notifier.parseArguments(jco,args);}
		catch (ParseException e) {logger.error(e.getMessage());jco.help();}
		catch (UtilsProcessingException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		catch (ExlpUnsupportedOsException e) {e.printStackTrace();}
		catch (NamingException e) {e.printStackTrace();}
		catch (SQLException e) {e.printStackTrace();}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		catch (JeeslLockingException e) {e.printStackTrace();}
		catch (Exception e) {e.printStackTrace();}
	}
}