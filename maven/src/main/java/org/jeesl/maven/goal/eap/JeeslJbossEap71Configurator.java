package org.jeesl.maven.goal.eap;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.controller.io.db.shell.mysql.MySqlShellCommands;
import org.jeesl.controller.io.db.shell.postgres.PostgreSqlShellCommands;
import org.jeesl.controller.io.ssi.wildfly.ds.AbstractEapDsConfigurator;

import net.sf.exlp.exception.ExlpUnsupportedOsException;

@Mojo(name="eap71Config")
public class JeeslJbossEap71Configurator extends AbstractJbossEapConfigurator
{	
	public JeeslJbossEap71Configurator()
	{
		eapVersion = "7.1";
	}
	
    public void execute() throws MojoExecutionException
    {	
		try
		{
			configureEap(super.config());
		}
		catch (Exception ex) {throw new MojoExecutionException(ex.getClass().toGenericString() +": " +ex.getMessage());}
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
	    	AbstractEapDsConfigurator.DbType dbType = AbstractEapDsConfigurator.DbType.valueOf(config.getString("db."+key+".type"));
        	switch(dbType)
        	{
        		case mysql: System.out.println(MySqlShellCommands.dropDatabase("root",pDbRootPwd,pDbName));
        					System.out.println(MySqlShellCommands.createDatabase("root",pDbRootPwd,pDbName));
        					System.out.println(MySqlShellCommands.grantDatabase("root",pDbRootPwd,pDbName,pDbUser,pDbPwd));
        					System.out.println(MySqlShellCommands.restoreDatabase("root",pDbRootPwd,pDbName,pDbDump));
        					break;
        		case postgresql:	System.out.println(PostgreSqlShellCommands.createUser("postgres",pDbUser,pDbPwd));
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