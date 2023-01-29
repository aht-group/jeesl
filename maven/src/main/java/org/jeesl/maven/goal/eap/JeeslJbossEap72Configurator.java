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

@Mojo(name="eap72Config")
public class JeeslJbossEap72Configurator extends AbstractJbossEapConfigurator
{	
	public JeeslJbossEap72Configurator()
	{
		
	}
	
    public void execute() throws MojoExecutionException
    {
    	BasicConfigurator.configure();
    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel(log));

    	Configuration config = config();
		configureEap(config);
    }
    
    private void configureEap(Configuration config) throws MojoExecutionException
    {
    	String jbossDir = config.getString("eap.dir","/Volumes/ramdisk/jboss");
		File f = new File(jbossDir);
		getLog().info("JBoss EAP 7.2 directoy: "+f.getAbsolutePath());
    	
    	ModelControllerClient client;
    	JbossModuleConfigurator jbossModule = new JbossModuleConfigurator(JbossModuleConfigurator.Product.eap,"7.2",jbossDir);
    	try {client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9990);}
    	catch (UnknownHostException e) {throw new MojoExecutionException(e.getMessage());}
    	
    	JbossStandaloneConfigurator jbossConfig = new JbossStandaloneConfigurator(client);
    	
    	String key = config.getString("eap.configurations");
	    getLog().warn("Keys: "+key);
	    String[] keys = key.split("-");
	    
	    try
	    {
	    	dbFiles(keys,config,jbossModule);
	    	dbDrivers(keys,config,jbossConfig);
	    	dbDs(keys,config,jbossConfig);
	    }
	    catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
}