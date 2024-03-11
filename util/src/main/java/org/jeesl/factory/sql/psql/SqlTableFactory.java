package org.jeesl.factory.sql.psql;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlTableFactory <TAB extends JeeslDbMetaTable<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SqlTableFactory.class);
	
	public static String rename(String from, String to)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE ").append(from);
		sb.append(" RENAME TO ").append(to);
		sb.append(";");
		return sb.toString();
	}
	
	public static String changeSchema(String table, String schema)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE ").append(table);
		sb.append(" SET SCHEMA ").append(schema);
		sb.append(";");
		return sb.toString();
	}
}