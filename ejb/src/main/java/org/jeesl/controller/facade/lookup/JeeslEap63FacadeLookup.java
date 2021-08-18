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

public class JeeslEap63FacadeLookup implements JeeslFacadeLookup
{
	final static Logger logger = LoggerFactory.getLogger(JeeslEap63FacadeLookup.class);
	
	private String appName;
	private String moduleName;

	private String host;
	private int port;
	private String username,password;
	
	public JeeslEap63FacadeLookup(String appName, String moduleName)
	{
		this(appName,moduleName,null,4447,null,null);
	}
	public JeeslEap63FacadeLookup(String appName, String moduleName, String host)
	{
		this(appName,moduleName,host,4447,null,null);
	}
	public JeeslEap63FacadeLookup(String appName, String moduleName, String host, String username, String password)
	{
		this(appName,moduleName,host,4447,username,password);
	}
	public JeeslEap63FacadeLookup(String appName, String moduleName, String host, int port, String username, String password)
	{
		this.appName=appName;
		this.moduleName=moduleName;
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
		
		if(this.host==null){this.host="localhost";}
		
		Security.addProvider(new JBossSaslProvider());
		
		EJBClientConfiguration clientConfiguration = new PropertiesBasedEJBClientConfiguration(properties());
		ContextSelector<EJBClientContext> contextSelector = new ConfigBasedEJBClientContextSelector(clientConfiguration);

		EJBClientContext.setSelector(contextSelector);
	}
	   
	@SuppressWarnings("unchecked")
	@Override public <F extends Object> F lookup(Class<F> facade) throws NamingException
    {
        Context context = new InitialContext(properties());
 
        final String beanName = facade.getSimpleName()+"Bean";
        final String interfaceName = facade.getName();
        
        StringBuilder sb = new StringBuilder();
//        sb.append("ejb:");
        sb.append(appName).append("/");
        sb.append(moduleName).append("/");
        sb.append("/");
//        sb.append(distinctName).append("/");
        sb.append(beanName);
        sb.append("!").append(interfaceName);
        logger.info("Trying: "+sb.toString());
        return (F) context.lookup(sb.toString());
    }
   
	private Properties properties()
	{
		Properties properties = new Properties();
//		properties.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");  
		properties.setProperty("endpoint.name", "client-endpoint"); 
		properties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		properties.put(Context.PROVIDER_URL, "remote://localhost:4447");
		properties.put("jboss.naming.client.ejb.context", true);

		properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
		properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
		if(username!=null){properties.put("remote.connection.default.username", username);}
		if(password!=null){properties.put("remote.connection.default.password", password);}
		
		properties.setProperty("org.jboss.ejb.client.scoped.context", "true");
		return properties;
   }
}