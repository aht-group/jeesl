package org.jeesl.controller.facade.lookup;

import java.security.Security;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;
import org.jboss.sasl.JBossSaslProvider;
import org.jeesl.api.facade.JeeslFacadeLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslWildflyFacadeLookup implements JeeslFacadeLookup
{
	final static Logger logger = LoggerFactory.getLogger(JeeslWildflyFacadeLookup.class);

	private boolean debugOnInfo; @Override public boolean setDebugOnInfo(boolean value) {return this.debugOnInfo=value;}
	
	private String appName;
	private String moduleName;
	private String localeCode; @Override public String getLocaleCode() {return localeCode;}
	private final Properties properties = new Properties();

	public JeeslWildflyFacadeLookup(String appName, String moduleName)
	{
		this(appName,moduleName,null,8080,null,null);
	}
	public JeeslWildflyFacadeLookup(String appName, String moduleName, String host)
	{
		this(appName,moduleName,host,8080,null,null);
	}
	public JeeslWildflyFacadeLookup(String appName, String moduleName, String host, String username, String password)
	{
		this(appName,moduleName,host,8080,username,password);
	}
	public JeeslWildflyFacadeLookup(String appName, String moduleName, String host, int port, String username, String password)
	{
		this.appName=appName;
		this.moduleName=moduleName;
		localeCode = "en";
		
		Security.addProvider(new JBossSaslProvider());
		
		properties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
		properties.put("remote.connections", "default");

		if(host==null){host="localhost";}
		logger.info("Connecting to "+host);
		properties.put("remote.connection.default.host", host);
		properties.put("remote.connection.default.port", Integer.valueOf(port).toString());

		properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
		if(username!=null){properties.put("remote.connection.default.username", username);}
		if(password!=null){properties.put("remote.connection.default.password", password);}
		
		EJBClientConfiguration clientConfiguration = new PropertiesBasedEJBClientConfiguration(properties);
		ContextSelector<EJBClientContext> contextSelector = new ConfigBasedEJBClientContextSelector(clientConfiguration);

		EJBClientContext.setSelector(contextSelector);
	}
	   
	@SuppressWarnings("unchecked")
	public <F extends Object> F lookup(Class<F> facade) throws NamingException
    {
        final Context context = createContext(properties);
        
        final String distinctName = "";
 
        final String beanName = facade.getSimpleName()+"Bean";
        final String viewClassName = facade.getName();
        
        StringBuilder sb = new StringBuilder();
        sb.append("ejb:");
        sb.append(appName).append("/");
        sb.append(moduleName).append("/");
        sb.append(distinctName).append("/");
        sb.append(beanName);
        sb.append("!").append(viewClassName);
        if(debugOnInfo) { logger.info("Trying: "+sb.toString());}
        return (F) context.lookup(sb.toString());
    }
   
   private Context createContext(Properties properties) throws NamingException
   {
		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
//		jndiProperties.put(Context.SECURITY_PRINCIPAL, properties.getProperty("remote.connection.default.username"));
//		jndiProperties.put(Context.SECURITY_CREDENTIALS, properties.getProperty("remote.connection.default.password"));
		String url = "remote://"+ properties.getProperty("remote.connection.default.host")+":"+  properties.getProperty("remote.connection.default.port");

		logger.info(url);
		jndiProperties.put(Context.PROVIDER_URL, url);
		return new InitialContext(jndiProperties);
   }
}