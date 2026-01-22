package org.jeesl.controller.io.db.shell.postgres;

import java.util.Arrays;
import java.util.List;

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

public class PostgresDrop extends AbstractPostgresShell implements UtilsDbShell
{
	final static Logger logger = LoggerFactory.getLogger(PostgresDrop.class);
	
	public PostgresDrop(Configuration config)
    {
		this(config, null);
    }
	public PostgresDrop(Configuration config,Document xmlConfig)
    {
		super(config, UtilsDbShell.Operation.restore, xmlConfig);
    }
	
	public void writeShell() throws ExlpUnsupportedOsException
	{
		operation=UtilsDbShell.Operation.drop;
		buildCommands(false);
		this.save();
	}
	
	public void buildCommands(boolean withStructure) throws ExlpUnsupportedOsException
	{		
		super.cmdPre();

		if(xmlConfig!=null)
		{
            XPathExpression<Element> xpe = XPathFactory.instance().compile("/config/db/tables/drop", Filters.element());
            List<Element> elementList = xpe.evaluateFirst(xmlConfig).getChildren();
            for(Element e : elementList)
            {
                if(e.getName().equals("table")) {dropTable(e.getText(),false);}
                else if(e.getName().equals("cascade")){dropTable(e.getText(),true);}
                else if(e.getName().equals("sequence")){dropSequence(e.getText());}
                else if(e.getName().equals("column")){dropColumn(e.getText());}
                else
                {
                	logger.warn("Unknwon element: "+e.getName());
                }
            }
		}
		else
		{
			logger.warn("Config: NYI: getStringArray");
//			for(String table : Arrays.asList(config.getStringArray(UtilsDbShell.cfgDbDropTable))){dropTable(table,false);}
//			for(String table : Arrays.asList(config.getStringArray(UtilsDbShell.cfgDbDropCascade))){dropTable(table,true);}
//			for(String seq : Arrays.asList(config.getStringArray(UtilsDbShell.cfgDbDropSequence))){dropSequence(seq);}
		}
		
		debugDatabase();
		super.cmdPost();
	}
	
	private String dropTable(String table, boolean cascade)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(pDbShell.getValue());
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" -c \"");
		sb.append("DROP TABLE IF EXISTS ").append(table);
		if(cascade){sb.append(" CASCADE");}
		sb.append(";\"");
		
		super.addLine(sb.toString());
		return sb.toString();
	}
	
	private String dropColumn(String text)
	{
		String[] s = text.split(",");
		return dropColumn(s[0],s[1]);
	}
	
	private String dropColumn(String table, String column)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(pDbShell.getValue());
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" -c \"");
		sb.append("ALTER TABLE ").append(table).append(" DROP COLUMN IF EXISTS ").append(column);
		sb.append(";\"");
		
		super.addLine(sb.toString());
		return sb.toString();
	}
	
	private String dropSequence(String seq)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(pDbShell.getValue());
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" -c \"");
		sb.append("DROP SEQUENCE IF EXISTS ").append(seq);
		sb.append(";\"");
		
		super.addLine(sb.toString());
		return sb.toString();
	}
	
	private String debugDatabase()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(pDbShell.getValue());
		sb.append(" -h ").append(pDbHost.getValue());
		sb.append(" -U ").append(pDbUser.getValue());
		sb.append(" -d ").append(pDbName.getValue());
		sb.append(" -c \"");
		sb.append("\\d ");
		sb.append(";\"");
		
		super.addLine(sb.toString());
		return sb.toString();
	}
}
