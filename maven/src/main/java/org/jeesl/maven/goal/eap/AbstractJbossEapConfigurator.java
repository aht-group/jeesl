package org.jeesl.maven.goal.eap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jeesl.processor.JbossModuleConfigurator;
import org.jeesl.processor.JbossStandaloneConfigurator;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.config.ConfigLoader;
import net.sf.exlp.util.io.ExlpCentralConfigPointer;
import net.sf.exlp.util.xml.JaxbUtil;

public abstract class AbstractJbossEapConfigurator extends AbstractMojo
{
	@Parameter(defaultValue = "INFO")
    protected String log;
	
	protected enum DbType {mysql,mariadb,postgres}
	protected final Set<DbType> setFiles;
	protected String eapVersion;
	
	public AbstractJbossEapConfigurator()
	{
		setFiles = new HashSet<DbType>();
	}
	
    protected Configuration config()
    {
    	String subnetConfigPrefix = test();
    	
    	ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance("jeesl").jaxb(JaxbUtil.instance());
    	try
		{
			ConfigLoader.add(ccp.toFile("eapConfig"+subnetConfigPrefix));
		}
		catch (ExlpConfigurationException e) {getLog().info("No specific "+ExlpCentralConfigPointer.class.getSimpleName()+" for "+subnetConfigPrefix);}
    	
    	try
		{
    		ConfigLoader.add(ccp.toFile("eapConfig"));
		}
		catch (ExlpConfigurationException e) {getLog().error("No additional "+ExlpCentralConfigPointer.class.getSimpleName()+" "+e.getMessage());}
		
		Configuration config = ConfigLoader.init();
		getLog().info("Using Config "+config.getString("jeesl.classifier","--"));
		return config;
    }
    
    private String test()
    {
    	try
    	{
    		Socket socket = new Socket();
			socket.connect(new InetSocketAddress("google.com", 80));
			String outbondIp = socket.getLocalAddress().getHostAddress();
			socket.close();
			
			System.out.println(outbondIp.lastIndexOf("."));
			
			String x = outbondIp.substring(0, outbondIp.lastIndexOf("."));
			
			getLog().info("Determined outbound IP "+outbondIp+" and checking config -"+x);
			return "-"+x;
		}
    	catch (IOException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "";
    }
    
    protected void dbFiles(String[] keys, Configuration config, JbossModuleConfigurator jbConfigurator) throws IOException
    {
    	List<String> log = new ArrayList<String>();
    	for(String key : keys)
    	{
        	DbType dbType = DbType.valueOf(config.getString("db."+key+".type"));
        	switch(dbType)
        	{
	        	case mariadb: if(!setFiles.contains(dbType)) {jbConfigurator.mariaDB();} break;
        		case mysql: if(!setFiles.contains(dbType)) {jbConfigurator.mysql();} break;
        		case postgres: if(!setFiles.contains(dbType)) {jbConfigurator.postgres(); jbConfigurator.hibernate();} break;
        	}
        	log.add(dbType.toString());
			setFiles.add(dbType);
    	}
    	super.getLog().info("DB Files: "+StringUtils.join(log, ", "));
    }
    
    protected void dbDs(String[] keys, Configuration config, JbossStandaloneConfigurator jbossConfig) throws IOException
    {
    	for(String key : keys)
    	{
    		String type = config.getString("db."+key+".type");
        	DbType dbType = DbType.valueOf(type);
        	String logMsg=null;
        	switch(dbType)
        	{
        		case mariadb: logMsg = jbossConfig.createMariaDbDatasource(config,key); break;
        		case mysql: logMsg = jbossConfig.createMysqlDatasource(config,key); break;		
        		case postgres: logMsg = jbossConfig.createPostgresDatasource(config,key);break;
        	}
        	if(logMsg!=null) {getLog().info("DS: "+logMsg);}
    	}
    }
    
    protected void dbDrivers(String[] keys, Configuration config, JbossStandaloneConfigurator jbossStandalone) throws IOException
    {
    	List<String> log = new ArrayList<String>();
    	for(String key : keys)
    	{
    		String type = config.getString("db."+key+".type");
        	DbType dbType = DbType.valueOf(type);
        	switch(dbType)
        	{
        		case mariadb: if(!jbossStandalone.driverExists("mariadb")) {jbossStandalone.createMariadbDriver();} break;
        		case mysql: if(!jbossStandalone.driverExists("mysql")) {jbossStandalone.createMysqlDriver();} break;
        		case postgres:	if(!jbossStandalone.driverExists("postgres")){jbossStandalone.createPostgresDriver();} break;
        	}
        	log.add(dbType.toString());
    	}
    	getLog().info("DB Drivers: "+StringUtils.join(log, ", "));
    }
}