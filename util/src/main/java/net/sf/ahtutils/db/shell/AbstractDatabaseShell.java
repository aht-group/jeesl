package net.sf.ahtutils.db.shell;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.WordUtils;
import org.jdom2.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.db.UtilsDbShell;
import net.sf.exlp.exception.ExlpUnsupportedOsException;
import net.sf.exlp.factory.xml.config.XmlParameterFactory;
import net.sf.exlp.factory.xml.config.XmlParametersFactory;
import net.sf.exlp.interfaces.util.TextWriter;
import net.sf.exlp.shell.cmd.ShellCmdChmod;
import net.sf.exlp.shell.os.OsArchitectureUtil;
import net.sf.exlp.shell.os.OsBashFile;
import net.sf.exlp.shell.spawn.Spawn;
import net.sf.exlp.util.io.txt.ExlpTxtWriter;
import net.sf.exlp.xml.config.Parameter;
import net.sf.exlp.xml.config.Parameters;

public class AbstractDatabaseShell
{
	final static Logger logger = LoggerFactory.getLogger(AbstractDatabaseShell.class);
	
	protected Configuration config;
	protected Document xmlConfig;
	
	protected Parameter pDbShell,pDbDump,pDbRestore;
	protected Parameter pDbHost,pDbName,pDbUser,pDbPwd;
	protected Parameter pDirDump,pDirRestore;
	
	protected String dbSchema;
	
	protected List<String> tables; public List<String> getTables(){return tables;}
	
	protected ExlpTxtWriter txtWriter;
	
	public AbstractDatabaseShell(Configuration config,UtilsDbShell.Operation operation)
	{
		this(config,operation,null);
	}
	public AbstractDatabaseShell(Configuration config,UtilsDbShell.Operation operation,Document xmlConfig)
	{
		this.config=config;
		this.operation=operation;
		this.xmlConfig=xmlConfig;
		
		configurationParamter = XmlParametersFactory.build();
		
		pDbHost = XmlParameterFactory.build("db."+operation.toString()+".host", "DB Host for "+operation.toString(), false);
		try{pDbHost.setValue(config.getString(pDbHost.getKey()));}
		catch (NoSuchElementException e){pDbHost.setValue("localhost");}
		configurationParamter.getParameter().add(pDbHost);
		
		pDbName = XmlParameterFactory.build("db."+operation.toString()+".db", "DB Name for "+operation.toString(), true);
		pDbName.setValue(config.getString(pDbName.getKey()));
		configurationParamter.getParameter().add(pDbName);
		
		pDbUser = XmlParameterFactory.build("db."+operation.toString()+".user.name", "DB User for athentication of "+operation.toString(), true);
		pDbUser.setValue(config.getString(pDbUser.getKey()));
		configurationParamter.getParameter().add(pDbUser);
		
		pDbPwd = XmlParameterFactory.build("db."+operation.toString()+".user.password", "DB Password for athentication of "+operation.toString(), true);
		pDbPwd.setValue(config.getString(pDbPwd.getKey()));
		configurationParamter.getParameter().add(pDbPwd);
		
		pDirRestore = XmlParameterFactory.build(UtilsDbShell.dirRestore, "Directory for restore file", true);
		pDirRestore.setValue(config.getString(pDirRestore.getKey()));
		configurationParamter.getParameter().add(pDirRestore);
		
		pDirDump = XmlParameterFactory.build(UtilsDbShell.dirRestore, "Directory for dump file", true);
		pDirDump.setValue(config.getString(pDirDump.getKey()));
		configurationParamter.getParameter().add(pDirDump);
		
		try{dbSchema = config.getString("db."+operation.toString()+".schema");}
		catch (NoSuchElementException e){}
		
		txtWriter = new ExlpTxtWriter();
		
		try
		{
			txtWriter.add(OsBashFile.prefix());
			txtWriter.add(OsBashFile.comment("Automaticall generated script for SQL "+operation.toString()));
		}
		catch (ExlpUnsupportedOsException e) {e.printStackTrace();}
		txtWriter.add("");
		txtWriter.add("");
	}
	
	public void debug()
	{
		logger.info("Bin: "+pDbShell.getValue()+" ("+UtilsDbShell.cfgBinDump+")");
		logger.info("Host: "+pDbHost.getValue()+" (db."+operation.toString()+".user)");
		logger.info("DB: "+pDbName.getValue()+" (db."+operation.toString()+".db)");
		logger.info("User: "+pDbUser.getValue()+" (db."+operation.toString()+".user)");
		logger.info("Pwd: "+pDbPwd.getValue()+" (db."+operation.toString()+".password)");
		if(dbSchema!=null){logger.info("Schema: "+dbSchema+" (db."+operation.toString()+".schema)");}
	}
	
	protected void addLine(String line)
	{
		txtWriter.add(line);
	}
	
	public TextWriter getWriter()
	{
		return txtWriter;
	}
	
	public File getShellFile()
	{
		String extension = "";
		String opScope = "";
		
		try {extension = "."+OsBashFile.fileExtention();} catch (ExlpUnsupportedOsException e) {e.printStackTrace();}
		
		if(scope!=null)
		{
			opScope = WordUtils.capitalize(scope.toString());
		}
		
		return new File(config.getString(UtilsDbShell.cfgDirShell),config.getString("db."+operation.toString()+".shell")+opScope+extension);
	}
	
	protected void save() throws ExlpUnsupportedOsException
	{
		File f = getShellFile(); 
		txtWriter.writeFile(f);
		
		if(OsArchitectureUtil.isUnixLike())
		{
			Spawn spawn = new Spawn(ShellCmdChmod.executeable(f));
			spawn.cmd();
		}
	}
	
	protected String getElementAfterLastDot(String cfg)
	{
		int index = cfg.lastIndexOf(".");
		String e = cfg.substring(index+1);
		logger.trace(e);
		return e;
	}
	
	//Configuration Parameter
	protected Parameters configurationParamter;
	public Parameters getConfigurationParameter(){return configurationParamter;}
	
	protected UtilsDbShell.Operation operation;
	public void setOperation(UtilsDbShell.Operation operation){this.operation = operation;}
	
	protected UtilsDbShell.Scope scope;
}
