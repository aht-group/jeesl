package org.jeesl.controller.io.ssi.wildfly.ds.pg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;
import org.jeesl.interfaces.controller.io.ssi.eap.EapDsConfigurator;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.util.MavenArtifactResolver;

public class DsPostgresEap72Configurator extends AbstractEapDsConfigurator implements EapDsConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(DsPostgresEap72Configurator.class);

	private DsPostgresEap71Configurator ds71Configurator;
	
	public static DsPostgresEap72Configurator instance(ModelControllerClient client) {return new DsPostgresEap72Configurator(client);}
	private DsPostgresEap72Configurator(ModelControllerClient client)
	{
		super(client);
		ds71Configurator = DsPostgresEap71Configurator.instance(client);
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
			FileUtils.copyInputStreamToFile(mrl.searchIs(AbstractEapDsConfigurator.srcBaseDir+"/eap/7.2/postgres.xml"),moduleXml);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgresql:postgresql:42.3.4"),moduleXml.getParentFile());
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("net.postgis:postgis-jdbc:2.5.0"),moduleXml.getParentFile());
			
			logger.info("\tAdded postgres module");
		}
	}
	
	public void moduleHibernate() throws IOException
	{
		File moduleXml = new File(super.buildMobuleMain("org/hibernate"),"module.xml");
		
		byte[] jeeslXml = IOUtils.toByteArray(mrl.searchIs(AbstractEapDsConfigurator.srcBaseDir+"/eap/7.2/hibernate.xml"));
		byte[] jbossXml = Files.readAllBytes(moduleXml.toPath());
		
		if(!Arrays.equals(jeeslXml,jbossXml))
		{
			Files.write(moduleXml.toPath(),jeeslXml);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.hibernate:hibernate-spatial:5.3.15.Final"),moduleXml.getParentFile());
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.geolatte:geolatte-geom:1.3.0"),moduleXml.getParentFile());
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("com.vividsolutions:jts-core:1.14.0"),moduleXml.getParentFile());
		
			logger.info("\tAdded hibernate module");
		}
	}
	
	@Override public void addDriver() throws IOException
	{	
		ds71Configurator.addDriver();
	}
	
	@Override public void addDatasource(JsonSsiCredential json) throws IOException
	{
		ds71Configurator.addDatasource(json);
	}
}