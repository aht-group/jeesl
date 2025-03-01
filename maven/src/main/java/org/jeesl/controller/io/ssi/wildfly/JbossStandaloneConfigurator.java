package org.jeesl.controller.io.ssi.wildfly;

import java.io.IOException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class JbossStandaloneConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(JbossStandaloneConfigurator.class);
	
	private final ModelControllerClient client;
	
//	private JbossStandaloneConfigurator(String eapVersion, InetAddress host, int port)
//	{
//		this(eapVersion, ModelControllerClient.Factory.create(host,9990));
//	}
	
	public JbossStandaloneConfigurator(String eapVersion, ModelControllerClient client)
	{
		this.client=client;
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
	
	public boolean driverExists(String name) throws IOException
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
	  	return result;
	}
	
	public void createMysqlDriver() throws IOException
	{			
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.ADD);
		request.get(ClientConstants.OP_ADDR).add("subsystem","datasources");
		request.get(ClientConstants.OP_ADDR).add("jdbc-driver","mysql");
		request.get("driver-name").set("mysql");
		request.get("driver-module-name").set("com.mysql");
		request.get("driver-xa-datasource-class-name").set("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
		client.execute(new OperationBuilder(request).build());
	}
	public void createMariadbDriver() throws IOException
	{			
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.ADD);
		request.get(ClientConstants.OP_ADDR).add("subsystem","datasources");
		request.get(ClientConstants.OP_ADDR).add("jdbc-driver","mariadb");
		request.get("driver-name").set("mariadb");
		request.get("driver-module-name").set("org.mariadb");
		request.get("driver-xa-datasource-class-name").set("org.mariadb.jdbc.MariaDbDataSource");
		client.execute(new OperationBuilder(request).build());
	}
	
	public String createMariaDbDatasource(org.exlp.interfaces.system.property.Configuration config, String context) throws IOException
	{
		String cfgDbDs = "db."+context+".ds";
		String cfgDbHost = "db."+context+".host";
		String cfgDbPort = "db."+context+".port";
		String cfgDbName = "db."+context+".db";
		String cfgDbUser = "db."+context+".user";
		String cfgDbPwd = "db."+context+".pwd";
		
		String pDbDs = config.getString(cfgDbDs);
		String pDbHost = config.getString(cfgDbHost);
		String pDbPort = config.getString(cfgDbPort,"3306");
		String pDbName = config.getString(cfgDbName);
		String pDbUser = config.getString(cfgDbUser);
		String pDbPwd = config.getString(cfgDbPwd);
		
		logger.debug(cfgDbPwd+"\t"+pDbDs);
		logger.debug(cfgDbHost+"\t"+pDbHost);
		logger.debug(cfgDbHost+"\t"+pDbHost);
		logger.debug(cfgDbName+"\t"+pDbName);
		logger.debug(cfgDbUser+"\t"+pDbUser);
		logger.debug(cfgDbPwd+"\t"+pDbPwd);
		
		boolean dsExists = dsExists(cfgDbDs);
		if(!dsExists)
		{
			createMariadbDatasource(pDbDs,pDbHost,pDbPort,pDbName,"?useSSL=false",pDbUser,pDbPwd);
			StringBuilder sb = new StringBuilder();
			sb.append(pDbDs);
			return sb.toString();
		}
		return null;
	}
	
	public String createMysqlDatasource(org.exlp.interfaces.system.property.Configuration config, String context) throws IOException
	{
		String cfgDbDs = "db."+context+".ds";
		String cfgDbHost = "db."+context+".host";
		String cfgDbPort = "db."+context+".port";
		String cfgDbName = "db."+context+".db";
		String cfgDbUser = "db."+context+".user";
		String cfgDbPwd = "db."+context+".pwd";
		
		String pDbDs = config.getString(cfgDbDs);
		String pDbHost = config.getString(cfgDbHost);
		String pDbPort = config.getString(cfgDbPort,"3306");
		String pDbName = config.getString(cfgDbName);
		String pDbUser = config.getString(cfgDbUser);
		String pDbPwd = config.getString(cfgDbPwd);
		
		logger.debug(cfgDbPwd+"\t"+pDbDs);
		logger.debug(cfgDbHost+"\t"+pDbHost);
		logger.debug(cfgDbHost+"\t"+pDbHost);
		logger.debug(cfgDbName+"\t"+pDbName);
		logger.debug(cfgDbUser+"\t"+pDbUser);
		logger.debug(cfgDbPwd+"\t"+pDbPwd);
		
		boolean dsExists = dsExists(cfgDbDs);
		if(!dsExists)
		{
			createMysqlDatasource(pDbDs,pDbHost,pDbPort,pDbName,"?useSSL=false",pDbUser,pDbPwd);
			StringBuilder sb = new StringBuilder();
			sb.append(pDbDs);
			return sb.toString();
		}
		return null;
	}
	
	public void createMariadbDatasource(String name, String host, String port, String db, String jdbcParamter, String username, String password) throws IOException
	{		
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.ADD);
		request.get(ClientConstants.OP_ADDR).add("subsystem","datasources");
		request.get(ClientConstants.OP_ADDR).add("data-source",name);
		
		datasource(request,name);
		connection(request,"mariadb",host,port,db,jdbcParamter);
		request.get("driver-name").set("mariadb");
		request.get("transaction-isolation").set("TRANSACTION_READ_COMMITTED");
		 
		pool(request);
		security(request,username,password);
		  
		request.get("prepared-statements-cache-size").set(32);
		request.get("share-prepared-statements").set(true);
		  
		client.execute(new OperationBuilder(request).build());
	}
	
	public void createMysqlDatasource(String name, String host, String port, String db, String jdbcParamter, String username, String password) throws IOException
	{		
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.ADD);
		request.get(ClientConstants.OP_ADDR).add("subsystem","datasources");
		request.get(ClientConstants.OP_ADDR).add("data-source",name);
		
		datasource(request,name);
		connection(request,"mysql",host,port,db,jdbcParamter);
		request.get("driver-name").set("mysql");
		request.get("transaction-isolation").set("TRANSACTION_READ_COMMITTED");
		 
		pool(request);
		security(request,username,password);
		  
		request.get("prepared-statements-cache-size").set(32);
		request.get("share-prepared-statements").set(true);
		  
		client.execute(new OperationBuilder(request).build());
	}
	
	private void datasource(ModelNode request, String name)
	{
		request.get("jta").set(true);
		request.get("jndi-name").set("java:jboss/datasources/"+name);
		request.get("pool-name").set(name);
		request.get("use-java-context").set(true);
		request.get("use-ccm").set(true);
		request.get("statistics-enabled").set(true);
	}
	
	private void connection(ModelNode request, String type, String host, String port, String db, String jdbcParamter)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:").append(type).append("://");
		sb.append(host);
		sb.append(":").append(port);
		sb.append("/").append(db);
		if(jdbcParamter!=null) {sb.append(jdbcParamter);}
		request.get("connection-url").set(sb.toString());
	}
	
	private void pool(ModelNode request)
	{
		request.get("min-pool-size").set(2);
		request.get("max-pool-size").set(5);
		request.get("pool-prefill").set(true);
		request.get("pool-use-strict-min").set(false);
		request.get("flush-strategy").set("FailingConnectionOnly");
	}
	
	private void security(ModelNode request, String username, String password)
	{
		request.get("user-name").set(username);
		request.get("password").set(password);
	}
}