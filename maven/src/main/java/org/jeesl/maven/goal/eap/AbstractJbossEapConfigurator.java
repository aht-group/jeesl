package org.jeesl.maven.goal.eap;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.controller.io.ssi.wildfly.JbossModuleConfigurator;
import org.jeesl.controller.io.ssi.wildfly.JbossStandaloneConfigurator;
import org.jeesl.controller.io.ssi.wildfly.cache.AbstractEapCacheConfigurator;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;
import org.jeesl.controller.io.ssi.wildfly.ds.pg.DsPostgresEap73Configurator;
import org.jeesl.interfaces.controller.io.ssi.eap.EapCacheConfigurator;
import org.jeesl.interfaces.controller.io.ssi.eap.EapDsConfigurator;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.config.ConfigLoader;
import net.sf.exlp.util.io.ExlpCentralConfigPointer;
import net.sf.exlp.util.xml.JaxbUtil;

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
	
    protected Configuration config()
    {
    	String subnetConfigPrefix = test();
    	
    	ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance("jeesl").jaxb(JaxbUtil.instance());
    	try
		{
			ConfigLoader.add(ccp.toFile("eapConfig"+subnetConfigPrefix));
		}
		catch (ExlpConfigurationException e) {getLog().info("No specific "+ExlpCentralConfigPointer.class.getSimpleName()+" for "+subnetConfigPrefix);}
    	
    	try
		{
    		ConfigLoader.add(ccp.toFile("eapConfig"));
		}
		catch (ExlpConfigurationException e) {getLog().error("No additional "+ExlpCentralConfigPointer.class.getSimpleName()+" "+e.getMessage());}
		
		Configuration config = ConfigLoader.init();
		
		String jbossDir = config.getString("eap.dir","/Volumes/ramdisk/jboss");
		File f = new File(jbossDir);
		for(EapDsConfigurator c : dsConfigurators.values()) {c.setJbossRoot(f);}
		
		super.getLog().info("Using Config "+config.getString("jeesl.classifier","--"));
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
    
    protected void configureEap(Configuration config) throws MojoExecutionException
    {
    	String jbossDir = config.getString("eap.dir","/Volumes/ramdisk/jboss");
		File f = new File(jbossDir);
		super.getLog().info("JBoss Configuration (EAP "+eapVersion+") base: "+f.getAbsolutePath());
    	
    	try
    	{
    		ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9990);
    		JbossModuleConfigurator jbossModule = new JbossModuleConfigurator(JbossModuleConfigurator.Product.eap,eapVersion,jbossDir);
    		JbossStandaloneConfigurator jbossConfig = new JbossStandaloneConfigurator(eapVersion,client);
    		
        	String key = config.getString("eap.configurations");
    	    super.getLog().info("Keys: "+key);
    	    String[] keys = key.split("-");
    	    
    	    this.dbFiles(keys,config,jbossModule);
    	    this.dbDrivers(keys,config);
    	    this.dbDs(keys,config);
    	    this.caches(keys,config,jbossConfig);
    	}
    	catch (UnknownHostException e) {throw new MojoExecutionException(e.getMessage());}
    	catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
    
    protected void dbFiles(String[] keys, Configuration config, JbossModuleConfigurator jbConfigurator) throws IOException
    {
    	logger.info("Module Configuration");
    	for(String key : keys)
    	{
    		String type = config.getString("db."+key+".type");
    		if(dsConfigurators.containsKey(type))
    		{
    			dsConfigurators.get(type).addModule();;
    		}
    		else {logger.error("No Configurator for "+type);}
    		
//    		AbstractEapDsConfigurator.DbType dbType = AbstractEapDsConfigurator.DbType.valueOf(config.getString("db."+key+".type"));
//        	switch(dbType)
//        	{
//	        	case mariadb: if(!setFiles.contains(dbType)) {jbConfigurator.mariaDB();} break;
//        		case mysql: if(!setFiles.contains(dbType)) {jbConfigurator.mysql();} break;
//        		case postgresql: if(!setFiles.contains(dbType)) {jbConfigurator.postgres(); jbConfigurator.hibernate();} break;
//        	}
//        	log.add(dbType.toString());
//			setFiles.add(dbType);
    	}
    }
    
    protected void dbDrivers(String[] keys, Configuration config) throws IOException
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
    
    protected void dbDs(String[] keys, Configuration config) throws IOException
    {
    	logger.info("DS Configuration");
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
    
    protected void caches(String[] keys, Configuration config, JbossStandaloneConfigurator jbossConfig) throws IOException
    {
    	logger.info("Cache Configuration");
    	if(Objects.nonNull(cacheConfigurator))
		{
	    	for(String system : keys)
	    	{
	    		cacheConfigurator.addCaches(AbstractEapCacheConfigurator.toSystem(config,system));
	    	}
		}
    	else
    	{
    		logger.warn("No "+EapCacheConfigurator.class.getSimpleName()+" defined");
    	}
    }
}