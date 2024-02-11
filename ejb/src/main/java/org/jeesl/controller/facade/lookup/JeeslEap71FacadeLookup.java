package org.jeesl.controller.facade.lookup;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jeesl.api.facade.JeeslFacadeLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslEap71FacadeLookup implements JeeslFacadeLookup
{
	final static Logger logger = LoggerFactory.getLogger(JeeslEap71FacadeLookup.class);
	
	private boolean debugOnInfo; @Override public boolean setDebugOnInfo(boolean value) {return this.debugOnInfo=value;}
	
	private Context context;

	private final String appName;
	private final String moduleName;
	private String localeCode; @Override public String getLocaleCode() {return localeCode;}
	
	private String username; public String getUsername() {return username;} public void setUsername(String username) {this.username = username;}
	private String password; public String getPassword() {return password;} public void setPassword(String password) {this.password = password;}
	
	private String host; public String getHost() {return host;} public void setHost(String host) {this.host = host;}
	private int port; public int getPort() {return port;} public void setPort(int port) {this.port = port;}
	
	public JeeslEap71FacadeLookup(String appName)
	{
		this(appName,null);
	}
	public JeeslEap71FacadeLookup(String appName, String moduleName)
	{
		this.appName=appName;
		this.moduleName=moduleName;
		localeCode = "en";
		host = "localhost";
		port = 8080;
	}
	
	public void debug()
	{
		logger.info("Connection: "+host+":"+port+" user:"+username+" pwd:"+password);
	}

	@SuppressWarnings("unchecked")
	@Override public <F> F lookup(Class<F> facade) throws NamingException
	{
		buildContext();
		
		StringBuilder sb = new StringBuilder();
		sb.append("ejb:");
		if(moduleName==null) {sb.append("/");}
		sb.append(appName);
		if(moduleName!=null) {sb.append("/").append(moduleName);}
		sb.append("/").append(facade.getSimpleName()).append("Bean");
		sb.append("!").append(facade.getName());	
		if(debugOnInfo) {logger.info("Looking up: "+sb.toString());}
		
		return (F) context.lookup(sb.toString());
	}
	
	private void buildContext() throws NamingException
	{
		if(context==null)
		{
			Properties properties = new Properties();
			properties.put(Context.INITIAL_CONTEXT_FACTORY,  "org.wildfly.naming.client.WildFlyInitialContextFactory");
			properties.put(Context.PROVIDER_URL, String.format("%s://%s:%d", "remote+http", host, port));
			properties.put("jboss.naming.client.ejb.context", true);
			if(username!=null && password!=null)
			{
				properties.put(Context.SECURITY_PRINCIPAL, username);
				properties.put(Context.SECURITY_CREDENTIALS, password);
			}
			context =  new InitialContext(properties);
		}
	}
}