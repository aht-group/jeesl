package org.jeesl.factory.sql;

import java.util.List;

import javax.persistence.Table;

import org.jeesl.util.query.sql.JeeslSqlQuery;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public class SqlFactory
{
	public static <E extends Enum<E>> String sum(String item, E attribute, String as, boolean newLine)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("sum(").append(path(item,attribute)).append(")");
		sb.append(" AS ").append(as);
		newLine(newLine,sb);
		return sb.toString();
	}
	
	public static <E extends Enum<E>> String id(String item, E attribute)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(path(item,attribute)).append("_id");
		return sb.toString();
	}
	
	public static <E extends Enum<E>> String path(String alias, E attribute)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(alias).append(".").append(attribute.toString());
		return sb.toString();
	}
	
	public static <T extends EjbWithId> String delete(Class<T> c)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(c.getAnnotation(Table.class).name());
		return sb.toString();
	}
	
	public static String from(String table, String as, boolean newLine)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("FROM ").append(table).append(" AS ").append(as);
		newLine(newLine,sb);
		return sb.toString();
	}
	
	public static <E extends Enum<E>> String inIdList(String item, E attribute, List<EjbWithId> ids, boolean newLine)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(item).append(".").append(attribute).append("_id");
		sb.append(" IN (").append(JeeslSqlQuery.inIdList(ids)).append(")");
		newLine(newLine,sb);
		return sb.toString();
	}
	
	public static void limit(StringBuilder sb, boolean apply, int limit, boolean newLine)
	{
		if(apply)
		{
			sb.append("LIMIT ").append(limit);
			newLine(newLine,sb);
		}
	}
	
	private static void newLine(boolean newLine, StringBuilder sb)
	{
		if(newLine)
		{
			sb.append("\n");
		}
	}
	
	public static void valuesBool(boolean first, StringBuilder sb, boolean x)
	{
		if(!first) {sb.append(",");}
		if(x) {sb.append("'t'");}
		else {sb.append("'f'");}
	}
	
	public static <T extends EjbWithId> void valuesId(boolean first, StringBuilder sb, T id)
	{
		if(!first) {sb.append(",");}
		sb.append(id.getId());
	}
}