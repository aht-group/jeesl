package org.jeesl.controller.io.db.shell.postgres;

import java.io.File;
import java.util.NoSuchElementException;

import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.interfaces.controller.db.UtilsDbShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpUnsupportedOsException;
import net.sf.exlp.factory.xml.config.XmlParameterFactory;

public class PostgresDump extends AbstractPostgresShell implements UtilsDbShell
{
	final static Logger logger = LoggerFactory.getLogger(PostgresDump.class);
	String db;
	public PostgresDump(Configuration config)
	{
		super(config,UtilsDbShell.Operation.dump);
		
		pDbShell = XmlParameterFactory.build(UtilsDbShell.cfgBinDump, "Shell command for dump", false);
		try{pDbShell.setValue(config.getString(pDbShell.getKey()));}
		catch (NoSuchElementException e){pDbShell.setValue("pg_dump");}
		configurationParamter.getParameter().add(pDbShell);
	}
	
	/*
	 * added 2nd constructor for different db's
	 * @config normal config file
	 * @db String for db declaration / wis=mopwh
	 */
	public PostgresDump(Configuration config, String db)
	{
	    this(config);
	    this.db=db; 	
	}
	
	public void writeShell() throws ExlpUnsupportedOsException
	{
		operation=UtilsDbShell.Operation.dump;
		buildCommands(true);
		this.save();
	}
	
	public void buildCommands(boolean withStructure) throws ExlpUnsupportedOsException
	{
		super.cmdPre();
		dumpDatabase();
		super.cmdPost();
	}
	
	private String dumpDatabase()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(pDbShell.getValue());
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" --create");
		sb.append(" --blobs");
		sb.append(" --format=c");
		sb.append(" --verbose");
		sb.append(" --file=").append(pDirDump.getValue()+File.separator+pDbName.getValue()+".sql");
		sb.append(" ").append(pDbName.getValue());
		
		super.addLine(sb.toString());
		return sb.toString();
	}
}