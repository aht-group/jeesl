package org.jeesl.maven.goal.eap;

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
		try
		{
			configureEap(super.config());
		}
		catch (Exception ex) {throw new MojoExecutionException(ex.getClass().toGenericString() +": " +ex.getMessage());}
    }
}