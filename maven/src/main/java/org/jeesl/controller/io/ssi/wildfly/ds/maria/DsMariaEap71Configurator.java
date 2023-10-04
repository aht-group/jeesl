package org.jeesl.controller.io.ssi.wildfly.ds.maria;

import java.io.IOException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;
import org.jeesl.interfaces.controller.io.ssi.eap.EapDsConfigurator;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DsMariaEap71Configurator extends AbstractEapDsConfigurator implements EapDsConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(DsMariaEap71Configurator.class);

	public DsMariaEap71Configurator log(org.apache.maven.plugin.logging.Log log) {super.log=log; return this;}
	
	public static DsMariaEap71Configurator instance(ModelControllerClient client) {return new DsMariaEap71Configurator(client);}
	private DsMariaEap71Configurator(ModelControllerClient client)
	{
		super(client);
	}
	
	@Override public void addModule() throws IOException
	{

	}
	
	@SuppressWarnings("unused")
	private void modulePostgres() throws IOException
	{	
//		FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.mariadb.jdbc:mariadb-java-client:2.2.5"),dirMain);
	}

	@Override
	public void addDriver() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDatasource(JsonSsiCredential json) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	
}