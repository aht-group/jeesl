package org.jeesl.maven.goal.eap;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.controller.config.jboss.JbossStandaloneConfigurator;
import org.jeesl.controller.config.jboss.JbossModuleConfigurator;

@Mojo(name="eap80Config")
public class JeeslJbossEap80Configurator extends AbstractJbossEapConfigurator
{	
	public JeeslJbossEap80Configurator()
	{
		
	}
	
    public void execute() throws MojoExecutionException
    {
    	BasicConfigurator.configure();
    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel(log));

		configureEap(super.config());
    }
    
    private void configureEap(Configuration config) throws MojoExecutionException
    {
    	String jbossDir = config.getString("eap.dir","/Volumes/ramdisk/jboss");
		File f = new File(jbossDir);
		getLog().info("JBoss EAP 8.0 directoy: "+f.getAbsolutePath());
    	
    	ModelControllerClient client;
    	JbossModuleConfigurator jbossModule = new JbossModuleConfigurator(JbossModuleConfigurator.Product.eap,"8.0",jbossDir);
    	try
    	{
    		client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9990);
    		
    		JbossStandaloneConfigurator jbossConfig = new JbossStandaloneConfigurator(client);
        	
        	String key = config.getString("eap.configurations");
    	    getLog().warn("Keys: "+key);
    	    String[] keys = key.split("-");
    	    
    	    dbFiles(keys,config,jbossModule);
	    	dbDrivers(keys,config,jbossConfig);
	    	dbDs(keys,config,jbossConfig);
    	}
    	catch (UnknownHostException e) {throw new MojoExecutionException(e.getMessage());}
    	catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
}