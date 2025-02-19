package org.jeesl.controller.io.mail;

import java.io.FileNotFoundException;

import javax.naming.NamingException;

import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.client.app.JeeslBootstrap;
import org.jeesl.client.app.JeeslMailSpooler;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;

public class CliIoMailSpooler extends AbstractSmtpSpooler
{
	final static Logger logger = LoggerFactory.getLogger(CliIoMailSpooler.class);

	public static void main(String args[]) throws FileNotFoundException, UtilsConfigurationException, NamingException, ExlpConfigurationException
	{
		Configuration config = JeeslBootstrap.wrap();
		JeeslMailSpooler cli = new JeeslMailSpooler();
		
		cli.local(config);
	}
}