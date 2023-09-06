package org.jeesl.maven.goal.eap;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.processor.JbossModuleConfigurator;
import org.jeesl.processor.JbossStandaloneConfigurator;

@Mojo(name="eap74Config")
public class JeeslJbossEap74Configurator extends AbstractJbossEapConfigurator
{	
	public JeeslJbossEap74Configurator()
	{
		eapVersion = "7.4";
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
		super.getLog().info("JBoss EAP "+eapVersion+" directoy: "+f.getAbsolutePath()+" x2");
    	
    	ModelControllerClient client;
    	JbossModuleConfigurator jbossModule = new JbossModuleConfigurator(JbossModuleConfigurator.Product.eap,eapVersion,jbossDir);
    	try {client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9990);}
    	catch (UnknownHostException e) {throw new MojoExecutionException(e.getMessage());}
    	
    	JbossStandaloneConfigurator jbossConfig = new JbossStandaloneConfigurator(eapVersion,client);
    	
    	String key = config.getString("eap.configurations");
	    getLog().warn("Keys: "+key);
	    String[] keys = key.split("-");
	    
	    try
	    {
	    	super.dbFiles(keys,config,jbossModule);
	    	super.dbDrivers(keys,config,jbossConfig);
	    	super.dbDs(keys,config,jbossConfig);
	    	super.caches(keys,config,jbossConfig);
	    }
	    catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
}