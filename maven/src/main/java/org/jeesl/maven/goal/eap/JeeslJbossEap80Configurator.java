package org.jeesl.maven.goal.eap;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.controller.io.ssi.wildfly.cache.CacheEap80Configurator;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;
import org.jeesl.controller.io.ssi.wildfly.ds.pg.DsPostgresEap80Configurator;

@Mojo(name="eap80Config")
public class JeeslJbossEap80Configurator extends AbstractJbossEapConfigurator
{	
	public JeeslJbossEap80Configurator()
	{
		eapVersion = "8.0";
		
		try
		{
			ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9990);
			dsConfigurators.put(AbstractEapDsConfigurator.DbType.postgresql.toString(),  DsPostgresEap80Configurator.instance(client).log(super.getLog()));
			
			cacheConfigurator = CacheEap80Configurator.instance(client);
		}
		catch (UnknownHostException e) {e.printStackTrace();}
	}
	
    public void execute() throws MojoExecutionException
    {
		try
		{
			configureEap(super.config());
		}
		catch (Exception e) {throw new MojoExecutionException(e.getClass().toGenericString() +": " +e.getMessage());}
    }
}