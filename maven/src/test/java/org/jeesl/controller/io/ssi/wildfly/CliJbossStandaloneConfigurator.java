package org.jeesl.controller.io.ssi.wildfly;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.maven.plugin.MojoExecutionException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jeesl.controller.io.ssi.wildfly.cache.CacheEap73Configurator;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;
import org.jeesl.controller.io.ssi.wildfly.ds.pg.DsPostgresEap73Configurator;
import org.jeesl.controller.io.ssi.wildfly.logging.LoggingConfigurator;
import org.jeesl.factory.json.io.ssi.core.JsonSsiCredentialFactory;
import org.jeesl.factory.json.io.ssi.core.JsonSsiSystemFactory;
import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliJbossStandaloneConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(CliJbossStandaloneConfigurator.class);
	
	ModelControllerClient client;
	
	public CliJbossStandaloneConfigurator() throws UnknownHostException
	{
		logger.info("c");
		client = ModelControllerClient.Factory.create(InetAddress.getByName("127.0.0.1"), 9990);
	}
	
	public void driverExists() throws IOException
	{
		DsPostgresEap73Configurator dsc = DsPostgresEap73Configurator.instance(client);
		
		for(AbstractEapDsConfigurator.DbType type : AbstractEapDsConfigurator.DbType.values())
		{
			logger.info(type+" not available "+dsc.driverNotAvailable(type.toString()));
		}
	}
	
	public void dsExists() throws IOException
	{
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.ADD);
		request.get(ClientConstants.OP_ADDR).add("module");
		request.get("name").set("org.postgres");
		request.get("resources").set("postgresql-42.2.5.jar");
		request.get("dependencies").set("javax.api,javax.transaction.api");
	
		client.execute(new OperationBuilder(request).build());
	}
	
	public void infinispan() throws IOException, MojoExecutionException
	{		
		JsonSsiSystem system = JsonSsiSystemFactory.instance().fluent().code("ofx").credentials().json();
		system.getCredentials().add(JsonSsiCredentialFactory.build("menu"));
		system.getCredentials().add(JsonSsiCredentialFactory.build("icon"));

		CacheEap73Configurator.instance(client).addCaches(system);
	
//		<cache-container name="jeesl">
//			<transport lock-timeout="60000"/>
//			<replicated-cache name="menucrumb">
//        		<transaction mode="BATCH"/>
//        		</replicated-cache>
//        	<replicated-cache name="menusub">
//        		<transaction mode="BATCH"/>
//        	</replicated-cache>
//        </cache-container>

	}
	
	public void logging() throws IOException, MojoExecutionException
	{		
		JsonSsiSystem system = JsonSsiSystemFactory.instance().fluent().code("ofx333").credentials().json();
		system.getCredentials().add(JsonSsiCredentialFactory.build("menu"));
		system.getCredentials().add(JsonSsiCredentialFactory.build("icon"));

		LoggingConfigurator.instance(client).addLogger("JEESL", "INFO", "COLOR-PATTERN");
		boolean exists = LoggingConfigurator.instance(client).existsRootLoggerHandler("JEESL");
		logger.info("Does handler exist in root logger? " +exists);
		if (!exists) {LoggingConfigurator.instance(client).addHandlerToRootLogger("JEESL");}
		
	}
	
	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		CliJbossStandaloneConfigurator cli = new CliJbossStandaloneConfigurator();
		
//		cli.driverExists();
//		cli.dsExists();
//		cli.infinispan();
		cli.logging();
	}
}