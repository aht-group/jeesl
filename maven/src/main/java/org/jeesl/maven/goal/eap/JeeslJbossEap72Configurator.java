package org.jeesl.maven.goal.eap;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name="eap72Config")
public class JeeslJbossEap72Configurator extends AbstractJbossEapConfigurator
{	
	public JeeslJbossEap72Configurator()
	{
		eapVersion = "7.2";
	}
	
    public void execute() throws MojoExecutionException
    {
    	BasicConfigurator.configure();
    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel(log));

    	Configuration config = config();
		
		try {
			configureEap(super.config());
		} catch (Exception ex) {
			throw new MojoExecutionException(ex.getClass().toGenericString() +": " +ex.getMessage());
		}
    }
}