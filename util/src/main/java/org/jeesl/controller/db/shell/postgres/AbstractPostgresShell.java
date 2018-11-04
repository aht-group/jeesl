package org.jeesl.controller.db.shell.postgres;

import java.util.NoSuchElementException;

import org.apache.commons.configuration.Configuration;
import org.jdom2.Document;
import org.jeesl.controller.db.shell.AbstractDatabaseShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.db.UtilsDbShell;
import net.sf.exlp.exception.ExlpUnsupportedOsException;
import net.sf.exlp.factory.xml.config.XmlParameterFactory;
import net.sf.exlp.shell.cmd.ShellCmdExport;
import net.sf.exlp.shell.cmd.ShellCmdUnset;
import net.sf.exlp.xml.config.Parameter;

public class AbstractPostgresShell extends AbstractDatabaseShell
{
	final static Logger logger = LoggerFactory.getLogger(AbstractPostgresShell.class);
	
	protected boolean pwdSet;
	
	private static final String PGPWASSWORD = "PGPASSWORD";
	protected Parameter pDbSuperUser,pDbSuperPwd;
	
	public AbstractPostgresShell(Configuration config,UtilsDbShell.Operation operation)
	{
		this(config,operation,null);
	}
	
	public AbstractPostgresShell(Configuration config,UtilsDbShell.Operation operation,Document xmlConfig)
    {
		super(config,operation,xmlConfig);
		
		pDbShell = XmlParameterFactory.build(UtilsDbShell.cfgBinPsql, "Shell command psql", false);
		try{pDbShell.setValue(config.getString(pDbShell.getKey()));}
		catch (NoSuchElementException e){pDbShell.setValue("psql");}
		configurationParamter.getParameter().add(pDbShell);
    } 
	
	public void cmdPre() throws ExlpUnsupportedOsException
	{
		setPwd(pDbPwd.getValue());
		super.addLine("");
	}
	
	public void cmdPost() throws ExlpUnsupportedOsException
	{
		super.addLine("");
		unsetPwd();
	}
	
	protected void setPwd(String pwd) throws ExlpUnsupportedOsException
	{
		super.addLine(ShellCmdExport.export(PGPWASSWORD,pwd));
		pwdSet = true;
	}
	
	protected void unsetPwd() throws ExlpUnsupportedOsException
	{
		super.addLine(ShellCmdUnset.unset(PGPWASSWORD));
		pwdSet = false;
	}
}