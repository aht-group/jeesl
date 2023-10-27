package org.jeesl.controller.io.ssi.wildfly.ds.mysql;

import java.io.IOException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;
import org.jeesl.interfaces.controller.io.ssi.eap.EapDsConfigurator;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DsMysqlEap71Configurator extends AbstractEapDsConfigurator implements EapDsConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(DsMysqlEap71Configurator.class);

	public DsMysqlEap71Configurator log(org.apache.maven.plugin.logging.Log log) {super.log=log; return this;}
	
	public static DsMysqlEap71Configurator instance(ModelControllerClient client) {return new DsMysqlEap71Configurator(client);}
	private DsMysqlEap71Configurator(ModelControllerClient client)
	{
		super(client);
	}
	
	@Override public void addModule() throws IOException
	{

	}
	
	private void modulePostgres() throws IOException
	{	
//		if(!dirMain.exists()){dirMain.mkdirs();}
//		if(!moduleXml.exists())
//		{
//			String src = AbstractEapDsConfigurator.srcBaseDir+"/"+product+"/"+version+"/mysql.xml";
//			logger.info("Available?"+mrl.isAvailable(src)+" "+src+" to "+moduleXml.getAbsolutePath());
//			InputStream input = mrl.searchIs(src);
//			FileUtils.copyInputStreamToFile(input, moduleXml);
//		}
//		
//		if(version.equals("6.3"))
//		{
//			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("mysql:mysql-connector-java:5.1.29"),dirMain);
//		}
//		else if(version.equals("7.0"))
//		{
//			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("mysql:mysql-connector-java:5.1.43"),dirMain);
//		}
//		else if(version.equals("7.1"))
//		{
//			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("mysql:mysql-connector-java:5.1.46"),dirMain);
//			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("mysql:mysql-connector-java:8.0.15"),dirMain);
//		}
//		else
//		{
//			logger.warn("NO MySQL drivers defined in "+this.getClass().getSimpleName()+" for "+version);
//		}
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