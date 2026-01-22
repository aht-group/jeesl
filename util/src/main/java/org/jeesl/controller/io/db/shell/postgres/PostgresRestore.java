package org.jeesl.controller.io.db.shell.postgres;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;

import org.exlp.interfaces.system.property.Configuration;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jeesl.interfaces.controller.db.UtilsDbShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpUnsupportedOsException;
import net.sf.exlp.factory.xml.config.XmlParameterFactory;
import net.sf.exlp.shell.cmd.ShellCmdQuote;

public class PostgresRestore extends AbstractPostgresShell implements UtilsDbShell
{
	final static Logger logger = LoggerFactory.getLogger(PostgresRestore.class);
	
	public PostgresRestore(Configuration config)
    {
		this(config,null);
    }
	public PostgresRestore(Configuration config, Document xmlConfig)
    {
		super(config, UtilsDbShell.Operation.restore, xmlConfig);
		pDbRestore = XmlParameterFactory.build(UtilsDbShell.cfgBinRestore, "Shell command for restore", false);
		try{pDbRestore.setValue(config.getString(pDbRestore.getKey()));}
		catch (NoSuchElementException e){pDbRestore.setValue("pg_restore");}
		configurationParamter.getParameter().add(pDbRestore);
		
		pDbSuperUser = XmlParameterFactory.build("db."+operation.toString()+".super.name", "DB Superuser for athentication of "+operation.toString(), true);
		pDbSuperUser.setValue(config.getString(pDbSuperUser.getKey()));
		configurationParamter.getParameter().add(pDbSuperUser);
		
		pDbSuperPwd = XmlParameterFactory.build("db."+operation.toString()+".super.password", "DB Suepruser Password for athentication of "+operation.toString(), true);
		pDbSuperPwd.setValue(config.getString(pDbSuperPwd.getKey()));
		configurationParamter.getParameter().add(pDbSuperPwd);
    }
	
	@Deprecated
	public void buildCommands(boolean withStructure) throws ExlpUnsupportedOsException
	{
		buildRestoreDataScript();
	}
	
	public void writeShell() throws ExlpUnsupportedOsException
	{
		scope = UtilsDbShell.Scope.data;
		buildRestoreDataScript();
		this.save();
		
//		txtWriter.clear();
//		scope = UtilsDbShell.Scope.structure;
//		buildRestoreStructureScript();
//		this.save();
	}
	
	@SuppressWarnings("unused")
	private void buildRestoreStructureScript() throws ExlpUnsupportedOsException
	{		
		super.cmdPre();
		restoreStructure();
		super.cmdPost();
	}
	
	private void buildRestoreDataScript() throws ExlpUnsupportedOsException
	{		
		super.cmdPre();
		
		XPathExpression<Element> xpe = XPathFactory.instance().compile("/config/db/tables/restore", Filters.element());
        List<Element> elementList = xpe.evaluateFirst(xmlConfig).getChildren();
		for(Element e : elementList)
        {
            if(e.getName().equals(getElementAfterLastDot(UtilsDbShell.restoreTable))) {restoreTable(e.getText());}
            else if(e.getName().equals(getElementAfterLastDot(UtilsDbShell.restoreTrigger))){restoreTableDisabledTrigger(e.getText());}
        }
		for(Element e : elementList)
		{
			if(e.getName().equals(getElementAfterLastDot(UtilsDbShell.restroreSequence))){restoreSequence(e.getText());}
		}
		for(Element e : elementList)
		{
			if(e.getName().equals(getElementAfterLastDot("db.tables.restore.file")))
			{
				try
				{
					InputStream is = mrl.searchIs(e.getText());
					BufferedReader br = new BufferedReader(new InputStreamReader(is));

					String input = br.readLine();
					while(input != null)
					{
						if(!input.equals(""))
						{
							restoreIndex(input);
						}
						input = br.readLine();
					}
				}
				catch (FileNotFoundException e1) {e1.printStackTrace();}
				catch(IOException e1) {e1.printStackTrace();}
			}
		}
		
		super.cmdPost();
	}
	
	public String restoreTable(String table) throws ExlpUnsupportedOsException
	{	
		if(!pwdSet){setPwd(pDbPwd.getValue());}
		StringBuffer sb = new StringBuffer();
		sb.append(pDbRestore.getValue());
		sb.append(" --verbose");
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -p ").append(pDbPort.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" --no-privileges");
		sb.append(" --no-owner");
		sb.append(" --data-only");
		sb.append(" -t " + table.toLowerCase());
		
		StringBuilder sbFile = new StringBuilder();
		sbFile.append(pDirRestore.getValue());
		sbFile.append(File.separator);
		sbFile.append(pDbName.getValue());
		sbFile.append(".sql");
		sb.append(" ").append(ShellCmdQuote.quote(sbFile.toString()));
		
		super.addLine(sb.toString());
		return sb.toString();
	}
	
	public String restoreTableDisabledTrigger(String table) throws ExlpUnsupportedOsException
	{
		StringBuffer sb = new StringBuffer();
		sb.append(pDbRestore.getValue());
		sb.append(" --verbose");
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -p ").append(pDbPort.getValue());
		sb.append(" -U ").append(pDbSuperUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" --superuser=").append(pDbSuperUser.getValue());
		sb.append(" --disable-triggers");
		sb.append(" --no-privileges");
		sb.append(" --no-owner");
		sb.append(" --data-only");
		sb.append(" -t " + table.toLowerCase());
		
		StringBuilder sbFile = new StringBuilder();
		sbFile.append(pDirRestore.getValue());
		sbFile.append(File.separator);
		sbFile.append(pDbName.getValue());
		sbFile.append(".sql");
		sb.append(" ").append(ShellCmdQuote.quote(sbFile.toString()));
		
		// Trigger http://dba.stackexchange.com/questions/23000/disable-constraints-before-using-pg-restore-exe
		// http://www.postgresonline.com/special_feature.php?sf_name=postgresql83_pg_dumprestore_cheatsheet
		
		if(pwdSet){unsetPwd();}
		setPwd(pDbSuperPwd.getValue());

		super.addLine(sb.toString());
		unsetPwd();
		super.addLine("");
		return sb.toString();
	}
	
	public String restoreSequence(String seq) throws ExlpUnsupportedOsException
	{
		//http://stackoverflow.com/questions/244243/how-to-reset-postgres-primary-key-sequence-when-it-falls-out-of-sync
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(pDbShell.getValue());
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -p ").append(pDbPort.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" -c \"");
		
		if(seq.contains(","))
		{	
			String[] s = seq.split(",");
			sb.append("SELECT setval('"+s[0]+"', (");
			sb.append("SELECT max(id) FROM (SELECT MAX(id) as id FROM "+s[1]+" UNION ");
			sb.append("SELECT MAX(id) as id FROM "+s[2]);
			sb.append(") as id));");
		}
		else
		{
			String table = seq.substring(0,seq.indexOf("_"));
			sb.append("SELECT setval('"+seq.toLowerCase()+"', (SELECT MAX(id) FROM "+table+"));");
		}
		
		sb.append("\"");
		
		if(!pwdSet){setPwd(pDbPwd.getValue());}
		super.addLine(sb.toString());
		return sb.toString();
	}

	public String restoreIndex(String seq) throws ExlpUnsupportedOsException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(pDbShell.getValue());
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -p ").append(pDbPort.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" -c \"").append(seq).append("\"");

		if(!pwdSet){setPwd(pDbPwd.getValue());}
		super.addLine(sb.toString());
		return sb.toString();
	}
	
	public String fixPrimaryKey(String table)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(pDbShell.getValue());
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -p ").append(pDbPort.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" -c \"").append("ALTER TABLE ").append(table).append(" ADD PRIMARY KEY (id);").append("\"");
		
		super.addLine(sb.toString());
		return sb.toString();
	}
	
	public void restoreStructure() throws ExlpUnsupportedOsException
	{		
		StringBuffer sb = new StringBuffer();
		sb.append(pDbRestore.getValue());
		sb.append(" --verbose");
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -p ").append(pDbPort.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" --no-privileges");
		sb.append(" --no-owner");
		sb.append(" --schema-only");
		sb.append(" ").append(pDirRestore.getValue() + File.separator + pDbName.getValue() + ".sql");		
		super.addLine(sb.toString());
	}
}