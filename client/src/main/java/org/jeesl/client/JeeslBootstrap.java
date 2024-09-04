package org.jeesl.client;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbcp.BasicDataSource;
import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.controller.handler.system.property.ConfigLoader;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.exlp.util.jx.JaxbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;

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
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
		
		try
		{
			ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(App.jeesl).jaxb(JaxbUtil.instance());
			ConfigLoader.addFile(ccp.toFile("client"));
			ConfigLoader.addFile(ccp.toFile("eapConfig"));
		}
		catch (ExlpConfigurationException e) {logger.debug("No additional "+ExlpCentralConfigPointer.class.getSimpleName()+" because "+e.getMessage());}
		ConfigLoader.addString(configFile);
		
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