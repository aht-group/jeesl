package org.jeesl.controller.io.ssi.wildfly.ds;

import java.io.File;
import java.io.IOException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jeesl.factory.json.io.ssi.core.JsonSsiCredentialFactory;
import org.jeesl.factory.json.io.ssi.core.JsonSsiSystemFactory;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

public class AbstractEapDsConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(AbstractEapDsConfigurator.class);
	
	public static final String srcBaseDir = "jeesl/system/io/config/jboss";
	public enum DbType {mysql,mariadb,postgresql}
	
	protected final MultiResourceLoader mrl;
	
	protected org.apache.maven.plugin.logging.Log log;
	protected File jbossBaseDir; public void setJbossRoot(File rootJboss) {this.jbossBaseDir=rootJboss;}
	protected final ModelControllerClient client;
	
	public AbstractEapDsConfigurator(ModelControllerClient client)
	{
		this.client=client;
		
		mrl = MultiResourceLoader.instance();
	}
	
	protected File buildMobuleMain(String packageId)
	{		
		File modules = buildMobuleBase1(packageId);
		
		File f = modules.toPath().toFile();
		for(String s : packageId.split("/"))
		{
			f = new File(f,s);
			if(!f.exists()) {f.mkdir();}
//			logger.info(f.getAbsolutePath());
		}	
		
		
		File moduleBase = f.toPath().toFile();
		File moduleMain = new File(moduleBase,"main");
		
//		logger.info(moduleMain.getAbsolutePath());
		
		if(!moduleBase.exists()) {moduleBase.mkdir();}
		if(!moduleMain.exists()) {moduleMain.mkdir();}
		
		return moduleMain;
	}
	
	private File buildMobuleBase1(String packageId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("modules");
		sb.append(File.separator).append("system");
		sb.append(File.separator).append("layers");
		sb.append(File.separator).append("base");

		return new File(jbossBaseDir,sb.toString());
	}
	
	public boolean driverNotAvailable(String name) throws IOException
	{
		boolean result = false;
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.READ_RESOURCE_OPERATION);
	  	request.get(ClientConstants.RECURSIVE).set(true);
	  	request.get(ClientConstants.OP_ADDR).add("subsystem", "datasources");
	  	ModelNode responce = client.execute(new OperationBuilder(request).build());
	  	ModelNode drivers = responce.get(ClientConstants.RESULT).get("jdbc-driver");
	  	if(drivers.isDefined())
	  	{
	  		for (ModelNode driver : drivers.asList())
  			{
  				String driverName = driver.asProperty().getName();
  				if (driverName.equals(name))
  				{
  					result=true;
  					break;
  				}				
  			}
	  	}
	  	return !result;
	}
	
	public boolean dsExists(String name) throws IOException
	{
		boolean result = false;
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.READ_RESOURCE_OPERATION);
	  	request.get(ClientConstants.RECURSIVE).set(true);
	  	request.get(ClientConstants.OP_ADDR).add("subsystem", "datasources");
	  	ModelNode responce = client.execute(new OperationBuilder(request).build());
	  	ModelNode drivers = responce.get(ClientConstants.RESULT).get("data-source");
	  	if (drivers.isDefined())
	  	{
	  		for (ModelNode driver : drivers.asList())
  			{
  				String driverName = driver.asProperty().getName();
  				if (driverName.equals(name))
  				{
  					result=true;
  					break;
  				}				
  			}
	  	}
	  	return result;
	}
	
	public static JsonSsiCredential toCredential(org.exlp.interfaces.system.property.Configuration config, String context)
	{
		JsonSsiCredential json = JsonSsiCredentialFactory.build();
		json.setSystem(JsonSsiSystemFactory.build(config.getString("db."+context+".type")));
		json.setCode(config.getString("db."+context+".ds"));
		json.setHost(config.getString("db."+context+".host","localhost"));
		json.setPort(config.getInteger("db."+context+".port",5432));
		
		json.setUser(config.getString("db."+context+".user"));
		json.setPassword(config.getString("db."+context+".pwd"));
		json.setToken(config.getString("db."+context+".db"));
		
		return json;
	}
	
	protected void security(ModelNode request, JsonSsiCredential json)
	{
		request.get("user-name").set(json.getUser());
		request.get("password").set(json.getPassword());
	}
}