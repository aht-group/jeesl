package org.jeesl.factory.sql.psql;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSchema;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlConstraintFactory <SCHEMA extends JeeslDbMetaSchema<?,?>,
									TAB extends JeeslDbMetaTable<?,?,SCHEMA>,
									CON extends JeeslDbMetaConstraint<?,TAB,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SqlConstraintFactory.class);
	
	public String rename(CON from, CON to)
	{
		return SqlConstraintFactory.rename(from.getTable().getCode(),from.getCode(),to.getCode());
	}
	
	public List<String> drop(List<CON> constraints)
	{
		List<String> list = new ArrayList<>();
		for(CON c : constraints) {list.add(SqlConstraintFactory.drop(c.getTable().getCode(),c.getCode()));}
		return list;
	}
	
	public static String rename(String table, String from, String to)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE ").append(table);
		sb.append(" RENAME CONSTRAINT ").append(from);
		sb.append(" TO ").append(to);
		sb.append(";");
		return sb.toString();
	}
	
	public static String drop(String table, String constraint)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE ").append(table);
		sb.append(" DROP CONSTRAINT ").append(constraint);
		sb.append(";");
		return sb.toString();
	}
}