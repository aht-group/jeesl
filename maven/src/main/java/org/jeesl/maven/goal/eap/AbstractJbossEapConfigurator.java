package org.jeesl.maven.goal.eap;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.exlp.controller.handler.system.property.ConfigLoader;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.handler.system.property.ConfigBootstrap;
import org.jeesl.controller.io.ssi.wildfly.cache.AbstractEapCacheConfigurator;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;
import org.jeesl.controller.io.ssi.wildfly.ds.pg.DsPostgresEap73Configurator;
import org.jeesl.interfaces.controller.io.ssi.eap.EapCacheConfigurator;
import org.jeesl.interfaces.controller.io.ssi.eap.EapDsConfigurator;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;

public abstract class AbstractJbossEapConfigurator extends AbstractMojo
{
	final static Logger logger = LoggerFactory.getLogger(DsPostgresEap73Configurator.class);
	
	@Parameter(defaultValue="INFO")
    protected String log;
	
	protected final Map<String,EapDsConfigurator> dsConfigurators;
	protected EapCacheConfigurator cacheConfigurator;
	
	protected final Set<AbstractEapDsConfigurator.DbType> setFiles;
	protected String eapVersion;
	
	public AbstractJbossEapConfigurator()
	{
		dsConfigurators = new HashMap<>();
		setFiles = new HashSet<AbstractEapDsConfigurator.DbType>();		
	}
	
    protected org.exlp.interfaces.system.property.Configuration config()
    {
    	String subnetConfigPrefix = test();
    	
    	ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance("jeesl").jaxb(JaxbUtil.instance());
    	
    	ConfigBootstrap cb = ConfigBootstrap.instance();
    	
    	try
		{
    		cb.add(ccp.toFile("eapConfig"+subnetConfigPrefix).toPath());
//			ConfigLoader.addFile(ccp.toFile("eapConfig"+subnetConfigPrefix));
		}
		catch (ExlpConfigurationException e) {getLog().info("No specific "+ExlpCentralConfigPointer.class.getSimpleName()+" for "+subnetConfigPrefix);}
    	
    	try
		{
    		cb.add(ccp.toFile("eapConfig").toPath());
//    		ConfigLoader.addFile(ccp.toFile("eapConfig"));
		}
		catch (ExlpConfigurationException e) {getLog().error("No additional "+ExlpCentralConfigPointer.class.getSimpleName()+" "+e.getMessage());}
		
//    	org.exlp.interfaces.system.property.Configuration config = ConfigLoader.wrap(ConfigLoader.init());
    	org.exlp.interfaces.system.property.Configuration config = cb.wrap();
		
		String jbossDir = config.getString("eap.dir","/Volumes/ramdisk/jboss");
		File f = new File(jbossDir);
		for(EapDsConfigurator c : dsConfigurators.values()) {c.setJbossRoot(f);}
		
		logger.info("Using Config "+config.getString("jeesl.classifier","--"));
		return config;
    }
    
    private String test()
    {
    	try
    	{
    		Socket socket = new Socket();
			socket.connect(new InetSocketAddress("google.com", 80));
			String outbondIp = socket.getLocalAddress().getHostAddress();
			socket.close();
			
			System.out.println(outbondIp.lastIndexOf("."));
			
			String x = outbondIp.substring(0, outbondIp.lastIndexOf("."));
			
			getLog().info("Determined outbound IP "+outbondIp+" and checking config -"+x);
			return "-"+x;
		}
    	catch (IOException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "";
    }
    
    protected void configureEap(org.exlp.interfaces.system.property.Configuration config) throws MojoExecutionException, Exception
    {
    	String jbossDir = config.getString("eap.dir","/Volumes/ramdisk/jboss");
		File f = new File(jbossDir);
		super.getLog().info("JBoss Configuration (EAP "+eapVersion+") base: "+f.getAbsolutePath());
    	
    	try
    	{
        	String key = config.getString("eap.configurations");
    	    super.getLog().info("Keys: "+key);
    	    String[] keys = key.split("-");
    	    
    	    this.dbFiles(keys,config);
    	    this.dbDrivers(keys,config);
    	    this.dbDs(keys,config);
    	    this.caches(keys,config);
    	}
    	catch (UnknownHostException e) {throw new MojoExecutionException(e.getMessage());}
    	catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
    
    protected void dbFiles(String[] keys, org.exlp.interfaces.system.property.Configuration config) throws IOException
    {
    	logger.info("Module Configuration");
    	for(String key : keys)
    	{
    		String type = config.getString("db."+key+".type");
    		if(dsConfigurators.containsKey(type))
    		{
    			dsConfigurators.get(type).addModule();
    		}
    		else {logger.error("No Configurator for "+type);}
    	}
    }
    
    protected void dbDrivers(String[] keys, org.exlp.interfaces.system.property.Configuration config) throws IOException
    {
    	for(String key : keys)
    	{
    		String type = config.getString("db."+key+".type");
    		if(dsConfigurators.containsKey(type))
    		{
    			dsConfigurators.get(type).addDriver();
    		}
    		else {logger.error("No Configurator for "+type);}
    	}
    }
    
    protected void dbDs(String[] keys, org.exlp.interfaces.system.property.Configuration config) throws IOException
    {
    	logger.info("DS Configuration for {} systems",keys.length);
    	for(String key : keys)
    	{
    		JsonSsiCredential credential = AbstractEapDsConfigurator.toCredential(config,key);
    		if(dsConfigurators.containsKey(credential.getSystem().getCode()))
    		{
    			dsConfigurators.get(credential.getSystem().getCode()).addDatasource(credential);;
    		}
    		else {logger.error("No Configurator for "+credential.getSystem().getCode());}
    	}
    }
    
    protected void caches(String[] keys, org.exlp.interfaces.system.property.Configuration config) throws IOException, Exception
    {
    	logger.info("Cache Configuration for {} systems",keys.length);
    	if(Objects.nonNull(cacheConfigurator))
		{
	    	for(String system : keys)
	    	{
	    		logger.trace("System: "+system);
				cacheConfigurator.addCaches(AbstractEapCacheConfigurator.toSystem(config,system));
	    	}
		}
    	else
    	{
    		logger.warn("No "+EapCacheConfigurator.class.getSimpleName()+" defined");
    	}
    }
}