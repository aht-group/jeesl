package org.jeesl.controller.io.ssi.wildfly.ds.pg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;
import org.jeesl.factory.txt.io.ssi.core.TxtIoSsiCredentialFactory;
import org.jeesl.interfaces.controller.io.ssi.eap.EapDsConfigurator;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.util.MavenArtifactResolver;

public class DsPostgresEap71Configurator extends AbstractEapDsConfigurator implements EapDsConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(DsPostgresEap71Configurator.class);

	public DsPostgresEap71Configurator log(org.apache.maven.plugin.logging.Log log) {super.log=log; return this;}
	
	public static DsPostgresEap71Configurator instance(ModelControllerClient client) {return new DsPostgresEap71Configurator(client);}
	private DsPostgresEap71Configurator(ModelControllerClient client)
	{
		super(client);
	}
	
	@Override public void addModule() throws IOException
	{
		this.modulePostgres();
		this.moduleHibernate();
	}
	
	private void modulePostgres() throws IOException
	{	
		File moduleXml = new File(super.buildMobuleMain("org/postgresql"),"module.xml");
		if(!moduleXml.exists())
		{
			FileUtils.copyInputStreamToFile(mrl.searchIs(AbstractEapDsConfigurator.srcBaseDir+"/eap/7.1/postgres.xml"), moduleXml);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgis:postgis-jdbc:1.5.3"),moduleXml.getParentFile());
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgresql:postgresql:42.3.4"),moduleXml.getParentFile());
			
			logger.info("\tAdded postgres module");
		}
	}
	
	public void moduleHibernate() throws IOException
	{
		File moduleXml = new File(super.buildMobuleMain("org/hibernate"),"module.xml");
		
		byte[] jeeslXml = IOUtils.toByteArray(mrl.searchIs(AbstractEapDsConfigurator.srcBaseDir+"/eap/7.3/hibernate.xml"));
		byte[] jbossXml = Files.readAllBytes(moduleXml.toPath());
		
		if(!Arrays.equals(jeeslXml,jbossXml))
		{
			Files.write(moduleXml.toPath(),jeeslXml);
		
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.hibernate:hibernate-spatial:5.1.12.Final"),moduleXml.getParentFile());
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.geolatte:geolatte-geom:1.0.6"),moduleXml.getParentFile());
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("com.vividsolutions:jts:1.13"),moduleXml.getParentFile());

			logger.info("\tAdded hibernate module");
		}
	}
	
	@Override public void addDriver() throws IOException
	{	
		
		if(super.driverNotAvailable(AbstractEapDsConfigurator.DbType.postgresql.toString()))
		{
			ModelNode request = new ModelNode();
			request.get(ClientConstants.OP).set(ClientConstants.ADD);
			request.get(ClientConstants.OP_ADDR).add("subsystem","datasources");
			request.get(ClientConstants.OP_ADDR).add("jdbc-driver",AbstractEapDsConfigurator.DbType.postgresql.toString());
			request.get("driver-name").set(AbstractEapDsConfigurator.DbType.postgresql.toString());
			
			request.get("driver-module-name").set("org.postgresql");
			request.get("driver-xa-datasource-class-name").set("org.postgresql.xa.PGXADataSource");
			
			client.execute(new OperationBuilder(request).build());
			logger.info("\tAdded postgres driver");
		}
	}
	
	@Override public void addDatasource(JsonSsiCredential json) throws IOException
	{
		if(!super.dsExists(json.getCode()))
		{
			this.createDatasource(json);
			logger.info("\tAdded DS "+json.getCode()+" "+json.getHost()+":"+json.getPort());
		}
	}
	
	private void createDatasource(JsonSsiCredential json) throws IOException
	{		
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.ADD);
		request.get(ClientConstants.OP_ADDR).add("subsystem","datasources");
		request.get(ClientConstants.OP_ADDR).add("data-source",json.getCode());
		
		this.dsAttributes(request,json.getCode());
		
		request.get("connection-url").set(TxtIoSsiCredentialFactory.toJdbc(json));
		request.get("driver-name").set(AbstractEapDsConfigurator.DbType.postgresql.toString());
		request.get("transaction-isolation").set("TRANSACTION_READ_COMMITTED");
		 
		this.pool(request);
		this.security(request,json);
		  
		request.get("prepared-statements-cache-size").set(32);
		request.get("share-prepared-statements").set(true);
		
		//Validation
		// old:
		// request.get("check-valid-connection-sql").set("select 1");
		// newer/different method:
		request.get("valid-connection-checker class-name").set("org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker");
		request.get("background-validation").set(true);
		request.get("background-validation-millis").set(5000);
		  
		client.execute(new OperationBuilder(request).build());
	}
	
	private void dsAttributes(ModelNode request, String name)
	{
		request.get("jta").set(true);
		request.get("jndi-name").set("java:jboss/datasources/"+name);
		request.get("pool-name").set(name);
		request.get("use-java-context").set(true);
		request.get("use-ccm").set(true);
		request.get("statistics-enabled").set(true);
	}
	
	private void pool(ModelNode request)
	{
		request.get("min-pool-size").set(2);
		request.get("max-pool-size").set(5);
		request.get("pool-prefill").set(true);
		request.get("pool-use-strict-min").set(false);
		request.get("flush-strategy").set("FailingConnectionOnly");
	}
}