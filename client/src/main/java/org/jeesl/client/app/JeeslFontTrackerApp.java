package org.jeesl.client.app;

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
import org.jeesl.api.rest.rs.jx.io.JeeslIoMavenRest;
import org.jeesl.client.JeeslBootstrap;
import org.jeesl.controller.handler.cli.JeeslCliOptionHandler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.json.io.maven.JeeslFontFactory;
import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.exception.ExlpUnsupportedOsException;
import net.sf.exlp.interfaces.util.ConfigKey;

public class JeeslFontTrackerApp
{
	final static Logger logger = LoggerFactory.getLogger(JeeslFontTrackerApp.class);
	
	private final Configuration config;
	private JeeslCliOptionHandler handler;
	
	
	private Option oHost,oSystem;

	private String cfgUrl,cfgHost,cfgSystem;
	
	public JeeslFontTrackerApp(Configuration config)
	{
		this.config = config;
	}
	
	private JeeslIoMavenRest buildRest(String url)
	{
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget restTarget = client.target(url);
		return restTarget.proxy(JeeslIoMavenRest.class);
	}
	
	public void local()
	{	
		Configuration config = JeeslBootstrap.init();
		
		cfgUrl = config.getString(ConfigKey.netRestUrlLocal);
		cfgHost = "x";
		cfgSystem = "jeesl";
		
		this.debugConfig();
		
		JsonMavenGraph graph = JeeslFontFactory.build();
		graph.setCode(cfgHost);
		
		this.buildRest(cfgUrl).uploadFonts(graph);
	}
	
	private void createOptions()
	{
		handler.buildHelp();
		handler.buildDebug();
        
		oSystem = Option.builder("system").required(true).hasArg(true).argName("SYSTEM").desc("System identifier").build(); handler.getOptions().addOption(oSystem);
        oHost = Option.builder("host").required(true).hasArg(true).argName("HOST").desc("Host identifier").build(); handler.getOptions().addOption(oHost);
       
	}
	
	private void debugConfig()
	{
		logger.info("URL: "+cfgUrl);
		logger.info("Host: "+cfgHost);
		logger.info("System: "+cfgSystem);
	}
	
	public void parseArguments(JeeslCliOptionHandler jcoh, String args[]) throws Exception
	{
		this.handler = jcoh;
		
		this.createOptions();
		
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(handler.getOptions() , args); 
	     
		handler.handleHelp(line);
		handler.handleLog4j2(line);

		cfgUrl = config.getString(ConfigKey.netRestUrlProduction);
		cfgSystem = line.getOptionValue(oSystem.getOpt());
		cfgHost = line.getOptionValue(oHost.getOpt());

		debugConfig();
		
		JsonMavenGraph graph = JeeslFontFactory.build();
		graph.setCode(cfgHost);
		
		this.buildRest(cfgUrl).uploadFonts(graph);
	}
	
	public static void main(String args[]) throws FileNotFoundException, UtilsConfigurationException, NamingException, ExlpConfigurationException
	{
		Configuration config = JeeslBootstrap.init();
		JeeslFontTrackerApp app = new JeeslFontTrackerApp(config);
		
//		app.local(); System.exit(0);
		
		JeeslCliOptionHandler jco = new JeeslCliOptionHandler(org.jeesl.Version.class.getPackage().getImplementationVersion());
		jco.setogPaths("jeesl/client/config");
		
		try {app.parseArguments(jco,args);}
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