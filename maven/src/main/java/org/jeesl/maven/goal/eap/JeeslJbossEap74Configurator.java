package org.jeesl.maven.goal.eap;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.controller.io.ssi.wildfly.cache.CacheEap73Configurator;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;
import org.jeesl.controller.io.ssi.wildfly.ds.pg.DsPostgresEap74Configurator;

@Mojo(name="eap74Config")
public class JeeslJbossEap74Configurator extends AbstractJbossEapConfigurator
{	
	public JeeslJbossEap74Configurator()
	{
		eapVersion = "7.4";
		try
		{
			ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9990);
			dsConfigurators.put(AbstractEapDsConfigurator.DbType.postgresql.toString(),  DsPostgresEap74Configurator.instance(client));
			
			cacheConfigurator = CacheEap73Configurator.instance(client);
		}
		catch (UnknownHostException e) {e.printStackTrace();}
	}
	
	public void execute() throws MojoExecutionException
	{
		try
		{
			super.configureEap(super.config());
		}
		catch (Exception e) {throw new MojoExecutionException(e.getClass().toGenericString() +": " +e.getMessage());}
	}
}