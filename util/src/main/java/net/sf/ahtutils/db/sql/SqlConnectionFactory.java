package net.sf.ahtutils.db.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlConnectionFactory
{
	final static Logger logger = LoggerFactory.getLogger(SqlConnectionFactory.class);
	
	private static enum DbType{mysql,postgresql,sqlite};
	
	private Connection c;
	private String type,host,db,username,password;
	private int port;
	
	private Configuration config;
	
	public SqlConnectionFactory(Configuration config)
	{
		this.config=config;
	}
	public SqlConnectionFactory(org.apache.commons.configuration2.Configuration config)
	{
//		this.config=config;
		logger.error("NYI COnfig 2");
	}
	
	public Connection getConnection(String code)
	{
		logger.debug("Using connection: "+code);
		
		type = config.getString("net.db."+code+".type");
		DbType dbType = DbType.valueOf(type);
		logger.debug("type");
		
		switch(dbType)
		{
			case mysql: connectMySQL(code); break;
			case postgresql: buildPostgresSqlConnection(code); break;
			case sqlite: connectSqlite(code);
		}
		
		return c;
	}
	
	private void connectSqlite(String code)
	{
		db = config.getString("net.db."+code+".database");
	    try
	    {
	    	StringBuffer sb = new StringBuffer();
	    	sb.append("jdbc:sqlite:");
	    	sb.append(db);
	    	
	    	logger.info("Connecting ... "+sb.toString());
	    	
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:"+db);
		}
	    catch (ClassNotFoundException e) {logger.error(e.getMessage());}
	    catch (SQLException e) {logger.error(e.getMessage());}
	}

	private void connectMySQL(String code)
	{
		host = config.getString("net.db."+code+".host");
		db = config.getString("net.db."+code+".database");
		
		try{port = config.getInt("net.db."+code+".port");}
		catch (NoSuchElementException e){port=3306;}
		
		username = config.getString("net.db."+code+".username");
		password = config.getString("net.db."+code+".password");
		
	    try
	    {
	    	logger.debug("Connecting ... "+getConnInfo());
	    	Class.forName("com.mysql.jdbc.Driver");
			c=DriverManager.getConnection(getConnInfo(),username, password);
		}
	    catch (ClassNotFoundException e) {logger.error(e.getMessage());}
	    catch (SQLException e) {logger.error(e.getMessage());}
	}
		
	private void buildPostgresSqlConnection(String code)
	{
		String cnfPrefix = "net.db."+code+".";
		logger.info("Using configuration prefix "+cnfPrefix);
		host = config.getString(cnfPrefix+"host");
		db = config.getString(cnfPrefix+"database");
		
		try{port = config.getInt(cnfPrefix+"port");}
		catch (NoSuchElementException e){port=3306;}
		
		username = config.getString(cnfPrefix+"username");
		password = config.getString(cnfPrefix+"password");
		
		buildPostgreSqlConnection();
	}
	
	public Connection buildPostgresSqlConnection(String host, String db, String username, String password)
	{
		type = DbType.postgresql.toString();
		this.host=host;
		this.db=db;
		this.port=5432;
		this.username=username;
		this.password=password;
		buildPostgreSqlConnection();
		return c;
	}
	
	private void buildPostgreSqlConnection()
	{
		try
	    {
	    	logger.info("Connecting ... "+getConnInfo());
	    	Class.forName("org.postgresql.Driver");
			c=DriverManager.getConnection(getConnInfo(),username, password);
		}
	    catch (ClassNotFoundException e) {e.printStackTrace();logger.error(e.getMessage());}
	    catch (SQLException e) {logger.error(e.getMessage());}
	}
	
	public void disconnect()
	{
		logger.debug("Disconnecting ...");
		try {c.close();}
		catch (SQLException e) {logger.error(e.getMessage());}
	}
	
	private String getConnInfo()
	{
		StringBuffer sb = new StringBuffer();
			sb.append("jdbc:");
			sb.append(type+"://");
			sb.append(host+":");
			sb.append(port+"/");
			sb.append(db);
		return sb.toString();
	}
}