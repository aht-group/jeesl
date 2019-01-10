package org.jeesl.controller.config.jboss;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JbossConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(JbossConfigurator.class);
	
	private final ModelControllerClient client;
	
	public JbossConfigurator(InetAddress host, int port)
	{
		this(ModelControllerClient.Factory.create(host,9990));
	}
	
	public JbossConfigurator(ModelControllerClient client)
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
	
	public void createPostgresDriver() throws IOException
	{		
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.ADD);
		request.get(ClientConstants.OP_ADDR).add("subsystem","datasources");
		request.get(ClientConstants.OP_ADDR).add("jdbc-driver","postgres");
		request.get("driver-name").set("postgres");
		request.get("driver-module-name").set("org.postgresql");
		request.get("driver-xa-datasource-class-name").set("org.postgresql.xa.PGXADataSource");
		client.execute(new OperationBuilder(request).build());
	}
	
	
	public String createMysqlDatasource(Configuration config, String context) throws IOException
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
	
	public String createPostgresDatasource(Configuration config, String context) throws IOException
	{
		String cfgDbDs = "db."+context+".ds";
		String cfgDbHost = "db."+context+".host";
		String cfgDbName = "db."+context+".db";
		String cfgDbUser = "db."+context+".user";
		String cfgDbPwd = "db."+context+".pwd";
		
		String pDbDs = config.getString(cfgDbDs);
		String pDbHost = config.getString(cfgDbHost);
		String pDbName = config.getString(cfgDbName);
		String pDbUser = config.getString(cfgDbUser);
		String pDbPwd = config.getString(cfgDbPwd);
		
		logger.debug(cfgDbPwd+"\t"+pDbDs);
		logger.debug(cfgDbHost+"\t"+pDbHost);
		logger.debug(cfgDbName+"\t"+pDbName);
		logger.debug(cfgDbUser+"\t"+pDbUser);
		logger.debug(cfgDbPwd+"\t"+pDbPwd);
		
		if(!this.dsExists(cfgDbDs))
		{
			createPostgresDatasource(pDbDs,pDbHost,pDbName,null,pDbUser,pDbPwd);
			StringBuilder sb = new StringBuilder();
			sb.append(pDbDs);
			return sb.toString();
		}
		return null;
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
	
	public void createPostgresDatasource(String name, String host, String db, String jdbcParamter, String username, String password) throws IOException
	{		
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set(ClientConstants.ADD);
		request.get(ClientConstants.OP_ADDR).add("subsystem","datasources");
		request.get(ClientConstants.OP_ADDR).add("data-source",name);
		
		String port = "5432";
		
		datasource(request,name);
		connection(request,"postgresql",host,port,db,jdbcParamter);
		request.get("driver-name").set("postgres");
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
		  request.get("min-pool-size").set(5);
		  request.get("max-pool-size").set(20);
		  request.get("pool-prefill").set(true);
		  request.get("pool-use-strict-min").set(false);
		  request.get("flush-strategy").set("FailingConnectionOnly");
	}
	
	private void security(ModelNode request, String username, String password)
	{
		request.get("user-name").set(username);
		request.get("password").set(password);
	}
	
	public void close()
	{
		IOUtils.closeQuietly(client);
	}
}