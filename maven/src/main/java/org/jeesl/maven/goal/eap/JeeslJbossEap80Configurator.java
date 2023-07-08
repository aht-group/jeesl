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
import org.jeesl.processor.JbossModuleConfigurator;
import org.jeesl.processor.JbossStandaloneConfigurator;

@Mojo(name="eap80Config")
public class JeeslJbossEap80Configurator extends AbstractJbossEapConfigurator
{	
	public JeeslJbossEap80Configurator()
	{
		eapVersion = "8.0";
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
		super.getLog().info("JBoss Configuration (EAP "+eapVersion+") base: "+f.getAbsolutePath());
    	
    	try
    	{
    		ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9990);
    		JbossModuleConfigurator jbossModule = new JbossModuleConfigurator(JbossModuleConfigurator.Product.eap,eapVersion,jbossDir);
    		JbossStandaloneConfigurator jbossConfig = new JbossStandaloneConfigurator(eapVersion,client);
        	
        	String key = config.getString("eap.configurations");
    	    super.getLog().info("Keys: "+key);
    	    String[] keys = key.split("-");
    	    
    	    super.dbFiles(keys,config,jbossModule);
	    	super.dbDrivers(keys,config,jbossConfig);
	    	super.dbDs(keys,config,jbossConfig);
    	}
    	catch (UnknownHostException e) {throw new MojoExecutionException(e.getMessage());}
    	catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
}