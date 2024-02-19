package org.jeesl.controller.facade.lookup;

import java.security.Security;
import java.util.Hashtable;
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

public class JeeslEap61FacadeLookup implements JeeslFacadeLookup
{
	final static Logger logger = LoggerFactory.getLogger(JeeslEap61FacadeLookup.class);
	
	private boolean debugOnInfo; @Override public boolean setDebugOnInfo(boolean value) {return this.debugOnInfo=value;}
	
	private String appName;
	private String moduleName;
	private String localeCode; @Override public String getLocaleCode() {return localeCode;}

	private final Properties properties;
	
	public JeeslEap61FacadeLookup(String appName, String moduleName)
	{
		this(appName,moduleName,null,4447,null,null);
	}
	public JeeslEap61FacadeLookup(String appName, String moduleName, String host)
	{
		this(appName,moduleName,host,4447,null,null);
	}
	public JeeslEap61FacadeLookup(String appName, String moduleName, String host, String username, String password)
	{
		this(appName,moduleName,host,4447,username,password);
	}
	public JeeslEap61FacadeLookup(String appName, String moduleName, String host, int port, String username, String password)
	{
		this.appName=appName;
		this.moduleName=moduleName;
		localeCode = "en";
		
		properties = new Properties();
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
//		if(username!=null){properties.put(Context.SECURITY_PRINCIPAL, username);}
//		if(password!=null){properties.put(Context.SECURITY_CREDENTIALS, password);}
//		logger.info(username+" "+password);
		
		EJBClientConfiguration clientConfiguration = new PropertiesBasedEJBClientConfiguration(properties);
		ContextSelector<EJBClientContext> contextSelector = new ConfigBasedEJBClientContextSelector(clientConfiguration);

		EJBClientContext.setSelector(contextSelector);
	}
	   
	@SuppressWarnings("unchecked")
	@Override public <F extends Object> F lookup(Class<F> facade) throws NamingException
    {
        final Context context = createContext();
        
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
        if(debugOnInfo) { logger.trace("Trying: "+sb.toString());}
        return (F) context.lookup(sb.toString());
    }
   
   private Context createContext() throws NamingException
   {
       Hashtable<String,String> jndiProperties = new Hashtable<String,String>();
       jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//       jndiProperties.put(Context.PROVIDER_URL, "remote://" +host +":4447");
//       if(username!=null){jndiProperties.put(Context.SECURITY_PRINCIPAL, username);}
//       if(password!=null){jndiProperties.put(Context.SECURITY_CREDENTIALS, password);}
       return new InitialContext(jndiProperties);
   }
}