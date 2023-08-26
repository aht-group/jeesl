package org.jeesl.client;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.config.ConfigLoader;
import net.sf.exlp.util.io.ExlpCentralConfigPointer;
import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.xml.JaxbUtil;

public class JeeslBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslBootstrap.class);

	public enum App {jeesl}
	
	public final static String xmlConfig = "jeesl/client/config/jeesl.xml";
	
	public static Configuration init()
	{
		return init(xmlConfig);
		
	}
	
	public static Configuration init(String configFile)
	{
		LoggerInit loggerInit = new LoggerInit("log4j.xml");
		loggerInit.addAltPath("jeesl/client/config");
		loggerInit.init();
		
		try
		{
			ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(App.jeesl).jaxb(JaxbUtil.instance());
			ConfigLoader.add(ccp.toFile("client"));
			ConfigLoader.add(ccp.toFile("eapConfig"));
		}
		catch (ExlpConfigurationException e) {logger.debug("No additional "+ExlpCentralConfigPointer.class.getSimpleName()+" because "+e.getMessage());}
		ConfigLoader.add(configFile);
		
		return ConfigLoader.init();
	}
	
	public static BasicDataSource buildDatasource(Configuration config, String code)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("jdbc:postgresql://");
		sb.append(config.getString("db."+code+".host"));
		sb.append(":").append(config.getString("db."+code+".port"));
		sb.append("/").append(config.getString("db."+code+".db"));
//		sb.append("?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
		logger.info("Postgres Connection: "+sb.toString());
		
		BasicDataSource dsMeis = new BasicDataSource();
		dsMeis.setDriverClassName("org.postgresql.Driver");
		dsMeis.setUsername(config.getString("db."+code+".user"));
		dsMeis.setPassword(config.getString("db."+code+".pwd"));

		dsMeis.setUrl(sb.toString());
		
		return dsMeis;
	}
}