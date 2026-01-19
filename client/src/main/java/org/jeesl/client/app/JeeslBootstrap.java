package org.jeesl.client.app;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.exlp.util.jx.JaxbUtil;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jeesl.controller.handler.system.property.ConfigBootstrap;
import org.jeesl.util.web.RestLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;

public class JeeslBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslBootstrap.class);

	public enum App {jeesl}
	
	public final static String xmlConfig = "jeesl/system/property/jeesl.xml";
	
	public static org.exlp.interfaces.system.property.Configuration wrap() {return ConfigBootstrap.wrap(JeeslBootstrap.init());}
	public static Configuration init() {return JeeslBootstrap.init(xmlConfig);}

	public static Configuration init(String configFile)
	{
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
		
		ConfigBootstrap bootstrap = ConfigBootstrap.instance();
		try
		{
			ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance("jeesl").jaxb(JaxbUtil.instance());
			bootstrap.add(ccp.toFile("client").toPath());
			bootstrap.add(ccp.toFile("eapConfig").toPath());
		}
		catch (ExlpConfigurationException e) {logger.warn("No additional "+ExlpCentralConfigPointer.class.getSimpleName()+" because "+e.getMessage());}
		bootstrap.add(xmlConfig);

		Configuration config = bootstrap.combine();
		
		return config;
		
//		try
//		{
//			ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(App.jeesl).jaxb(JaxbUtil.instance());
//			ConfigLoader.addFile(ccp.toFile("client"));
//			ConfigLoader.addFile(ccp.toFile("eapConfig"));
//		}
//		catch (ExlpConfigurationException e) {logger.debug("No additional "+ExlpCentralConfigPointer.class.getSimpleName()+" because "+e.getMessage());}
//		ConfigLoader.addString(configFile);
//		
//		return ConfigLoader.init();
	}
	
	public static <T extends Object> T rest(Class<T> c)
	{
		return rest(c,"http://localhost:8080/jeesl");
	}
	public static <T extends Object> T rest(Class<T> c, String url)
	{
		Client client = ClientBuilder.newBuilder().build();
		client.register(new RestLogger());
		ResteasyWebTarget target = (ResteasyWebTarget)client.target(url);
		return target.proxy(c);
	}

	public static BasicDataSource buildDatasource(org.exlp.interfaces.system.property.Configuration config, String code)
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