package org.jeesl.maven.goal.eap;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.controller.config.jboss.JbossStandaloneConfigurator;
import org.jeesl.controller.config.jboss.JbossModuleConfigurator;
import org.jeesl.controller.db.shell.mysql.MySqlShellCommands;
import org.jeesl.controller.db.shell.postgres.PostgreSqlShellCommands;

import net.sf.exlp.exception.ExlpUnsupportedOsException;

@Mojo(name="eap71Config")
public class JeeslJbossEap71Configurator extends AbstractJbossEapConfigurator
{	
	public JeeslJbossEap71Configurator()
	{
		
	}
	
    public void execute() throws MojoExecutionException
    {
    	BasicConfigurator.configure();
    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel(log));

    	Configuration config = config();
//		try{dbRestore(config);}
//		catch (ExlpUnsupportedOsException e) {throw new MojoExecutionException(e.getMessage());}
		configureEap(config);
    }
    
    private void configureEap(Configuration config) throws MojoExecutionException
    {
    	String jbossDir = config.getString("eap.dir","/Volumes/ramdisk/jboss");
		File f = new File(jbossDir);
		getLog().info("JBoss EAP 7.1 directoy: "+f.getAbsolutePath());
    	
    	ModelControllerClient client;
    	JbossModuleConfigurator jbossModule = new JbossModuleConfigurator(JbossModuleConfigurator.Product.eap,"7.1",jbossDir);
    	try {client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9990);}
    	catch (UnknownHostException e) {throw new MojoExecutionException(e.getMessage());}
    	
    	JbossStandaloneConfigurator jbossConfig = new JbossStandaloneConfigurator(client);
    	
    	String key = config.getString("eap.configurations");
	    getLog().warn("Keys: "+key);
	    String[] keys = key.split("-");
	    
	    try
	    {
	    	dbFiles(keys,config,jbossModule);
	    	dbDrivers(keys,config,jbossConfig);
	    	dbDs(keys,config,jbossConfig);
	    }
	    catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
    
    @SuppressWarnings("unused")
	private void dbRestore(Configuration config) throws ExlpUnsupportedOsException
    {
	    String[] keys = config.getString("db.restores").split("-");
	    for(String key : keys)
    	{
	    	getLog().info("Starting DB Restore for "+key);
	    	String pDbName = config.getString("db."+key+".db");
	    	String pDbUser = config.getString("db."+key+".user");
	    	String pDbPwd = config.getString("db."+key+".pwd");
	    	String pDbDump = config.getString("db."+key+".dump");
	    	String pDbRootPwd = config.getString("db."+key+".rootpwd",null);
        	DbType dbType = DbType.valueOf(config.getString("db."+key+".type"));
        	switch(dbType)
        	{
        		case mysql: System.out.println(MySqlShellCommands.dropDatabase("root",pDbRootPwd,pDbName));
        					System.out.println(MySqlShellCommands.createDatabase("root",pDbRootPwd,pDbName));
        					System.out.println(MySqlShellCommands.grantDatabase("root",pDbRootPwd,pDbName,pDbUser,pDbPwd));
        					System.out.println(MySqlShellCommands.restoreDatabase("root",pDbRootPwd,pDbName,pDbDump));
        					break;
        		case postgres:	System.out.println(PostgreSqlShellCommands.createUser("postgres",pDbUser,pDbPwd));
        						System.out.println(PostgreSqlShellCommands.terminate("postgres",pDbName));
        						System.out.println(PostgreSqlShellCommands.terminate("postgres","template1"));
        						System.out.println(PostgreSqlShellCommands.dropDatabase("postgres",pDbName));
        						System.out.println(PostgreSqlShellCommands.createDatabase("postgres",pDbName,pDbUser));
        						System.out.println(PostgreSqlShellCommands.postGis("postgres",pDbName));
        						System.out.println(PostgreSqlShellCommands.restoreDatabase("postgres", pDbName, pDbDump));
        					break;
        		default: getLog().warn(dbType.toString()+" NYI");
        	}
    	}
    }
}