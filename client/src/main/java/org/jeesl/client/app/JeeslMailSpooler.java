package org.jeesl.client.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.Configuration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jeesl.api.rest.rs.io.mail.JeeslIoMailRest;
import org.jeesl.client.JeeslBootstrap;
import org.jeesl.controller.handler.cli.JeeslCliOptionHandler;
import org.jeesl.controller.mail.AbstractSmtpSpooler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.model.xml.system.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.exception.ExlpUnsupportedOsException;
import net.sf.exlp.interfaces.util.ConfigKey;
import net.sf.exlp.util.xml.JaxbUtil;

public class JeeslMailSpooler extends AbstractSmtpSpooler
{
	final static Logger logger = LoggerFactory.getLogger(JeeslMailSpooler.class);

	public JeeslMailSpooler()
	{
		
		
	}
	
	private void buildRest(String url)
	{
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget restTarget = client.target(url);
		rest = restTarget.proxy(JeeslIoMailRest.class);
	}
	
	public void local()
	{	
		Configuration config = JeeslBootstrap.init();
		
		cfgUrl = config.getString(ConfigKey.netRestUrlLocal);
		cfgSmtp = config.getString(ConfigKey.netSmtpHost);
		
		debugConfig();
		buildRest(cfgUrl);
		buildSmtp(cfgSmtp);
		
//		spooler();
//		discard();
		debug(true,false,true);
	}
	
	public void discard()
	{
		Mails xml = rest.discard(1);
		JaxbUtil.info(xml);
	}


	public void parseArguments(JeeslCliOptionHandler jco, String args[]) throws Exception
	{
		this.jco = jco;
		
		createOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(jco.getOptions() , args); 
	     
		jco.handleHelp(line);
		jco.handleLogger(line);
		
//		Configuration config = uOption.initConfig(line, MeisBootstrap.xmlConfig);
	    
		cfgUrl = line.getOptionValue(oUrl.getOpt());
		cfgSmtp = line.getOptionValue(oSmtp.getOpt());
		
		debugConfig();
		buildRest(cfgUrl);
		buildSmtp(cfgSmtp);
		spooler();
	}
	
	public static void main(String args[]) throws FileNotFoundException, UtilsConfigurationException, NamingException, ExlpConfigurationException
	{
		JeeslMailSpooler smtp = new JeeslMailSpooler();
		
		smtp.local(); System.exit(-1);
		
		JeeslCliOptionHandler jco = new JeeslCliOptionHandler(org.jeesl.Version.class.getPackage().getImplementationVersion());
		jco.setLog4jPaths("jeesl/client/config");
		
		try
		{
			smtp.parseArguments(jco,args);
		}
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