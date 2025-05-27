package org.jeesl.factory.sql;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.factory.txt.util.TxtIdFactory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.util.query.sql.JeeslSqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlFactory
{
	final static Logger logger = LoggerFactory.getLogger(SqlFactory.class);
	
	private StringBuilder sb;
	private String alias;
	private boolean newLine;
	
	private Map<Class<?>,String> mapAlias;
	private boolean envers; public SqlFactory envers() {envers=true; return this;}
	
	public static SqlFactory instance() {return new SqlFactory();}
	private SqlFactory()
	{
		sb = new StringBuilder();
		alias = null;
		newLine = false;
		envers = false;
		
		mapAlias = new HashMap<>();
	}
	
	public <T extends EjbWithId> SqlFactory alias(Class<T> c, String a) {mapAlias.put(c,a);return this;}
	
	public <T extends EjbWithId> SqlFactory from(Class<T> c)
	{
		String a = null;
		if(Objects.nonNull(alias)) {a=alias;}
		else if(mapAlias.containsKey(c)) {a = mapAlias.get(c);}
		
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append(" FROM ");
		if(envers) {sb.append("envers.");}
		sb.append(c.getAnnotation(Table.class).name());
		if(envers) {sb.append("_");}
		if(Objects.nonNull(a)) {sb.append(" AS ").append(a);}
		
		return this;
	}
	
	public <T extends EjbWithId, E extends Enum<E>> SqlFactory where(Class<T> c, E attribute, Long id)
	{
		sb.append(" WHERE ");
		if(Objects.nonNull(alias)) {sb.append(alias).append(".");} else if(mapAlias.containsKey(c)) {sb.append(mapAlias.get(c)).append(".");}
		sb.append(attribute.toString());
		sb.append(" = ").append(id);
		
		return this;
	}

	
	public <T extends EjbWithId> SqlFactory delete(Class<T> c)
	{
		SqlFactory.deleteFrom(sb,c,alias,newLine);
		return this;
	}
	
	public <C extends EjbWithId, E extends Enum<E>, T extends EjbWithId> SqlFactory update(Class<C> c, E attribute, T t)
	{
		SqlFactory.update(sb,c,alias,attribute,t,newLine);
		return this;
	}
	public <C extends EjbWithId, E extends Enum<E>, T extends EjbWithId> SqlFactory updateTime(Class<C> c, E attribute, LocalDateTime value)
	{
		SqlFactory.updateLdt(sb,c,alias,attribute,value,newLine);
		return this;
	}
	
	public <T extends EjbWithId> SqlFactory whereId(T where)
	{
		sb.append(" WHERE id=");
		sb.append(where.getId());
		return this;
	}

	public <E extends Enum<E>, T extends EjbWithId> SqlFactory where(E attribute, T where)
	{
		sb.append(" WHERE (");
		whereAndOrAttribute(sb,alias,false,attribute,where,newLine);
		sb.append(" )");
		return this;
	}
	public <E extends Enum<E>, T extends EjbWithId> SqlFactory whereNot(E attribute, T where)
	{
		sb.append(" WHERE (");
		whereAndOrAttribute(sb,alias,true,attribute,where,newLine);
		sb.append(" )");
		return this;
	}
	public <E extends Enum<E>, T extends EjbWithId> SqlFactory whereIn(E attribute, T... where)
	{
		sb.append(" WHERE ");
		sb.append(id(alias,attribute));
		sb.append(" IN (").append(TxtIdFactory.commaList(where)).append(")");
		return this;
	}
	public <E extends Enum<E>, T extends EjbWithId> SqlFactory and(E attribute, T where)
	{
		sb.append(" AND ");
		whereAndOrAttribute(sb,alias,false,attribute,where,newLine);
		return this;
	}
	public SqlFactory limit(int limit)
	{
		sb.append(" LIMIT ").append(limit);
		return this;
	}
	
	public <T extends EjbWithId, E extends Enum<E>> void join(String aliasOwner, E attribute, Class<T> c,  String aliasOther)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append(" JOIN ").append(c.getAnnotation(Table.class).name());
		sb.append(" ").append(aliasOther);
		sb.append(" ON ");
		sb.append(aliasOther).append(".id = ").append(aliasOwner).append(".").append(attribute.toString()).append("_id");
		newLine(newLine,sb);
	}
	
	public String toString() {sb.append(";"); return sb.toString();}
	
	public static <E extends Enum<E>, T extends EjbWithId> void update(StringBuilder sb, Class<?> c, String alias, E attribute, T t, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append("UPDATE ").append(c.getAnnotation(Table.class).name());
		if(alias!=null) {sb.append(" "+alias);}
		sb.append(" SET ").append(id(alias,attribute)).append("=");
		if(t!=null) {sb.append(t.getId());}
		else {sb.append("NULL");}
		newLine(newLine,sb);
	}
	public static <E extends Enum<E>, T extends EjbWithId> void updateSet(StringBuilder sb, String alias, E attribute, T t, boolean newLine)
	{
		sb.append(", ").append(id(alias,attribute)).append("=");
		if(t!=null) {sb.append(t.getId());}
		else {sb.append("NULL");}
		newLine(newLine,sb);
	}
	
	public static <E extends Enum<E>> void updateL(StringBuilder sb, Class<?> c, String alias, E attribute, Long value, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append("UPDATE ").append(c.getAnnotation(Table.class).name());
		if(alias!=null) {sb.append(" "+alias);}
		sb.append(" SET ").append(path(alias,attribute)).append("=");
		if(value!=null) {sb.append(value);}
		else {sb.append("NULL");}
		newLine(newLine,sb);
	}
	
	public static <E extends Enum<E>> void updateD(StringBuilder sb, Class<?> c, String alias, E attribute, Double value, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append("UPDATE ").append(c.getAnnotation(Table.class).name());
		if(alias!=null) {sb.append(" "+alias);}
		sb.append(" SET ").append(path(alias,attribute)).append("=");
		if(value!=null) {sb.append(value);}
		else {sb.append("NULL");}
		newLine(newLine,sb);
	}
	
	public static <E extends Enum<E>> void updateS(StringBuilder sb, Class<?> c, String alias, E attribute, String value, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append("UPDATE ").append(c.getAnnotation(Table.class).name());
		if(alias!=null) {sb.append(" "+alias);}
		sb.append(" SET ").append(path(alias,attribute)).append("=");
		if(value!=null) {sb.append("'").append(value).append("'");}
		else {sb.append("NULL");}
		newLine(newLine,sb);
	}
	public static <E extends Enum<E>> void updateLdt(StringBuilder sb, Class<?> c, String alias, E attribute, LocalDateTime value, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append("UPDATE ").append(c.getAnnotation(Table.class).name());
		if(alias!=null) {sb.append(" "+alias);}
		sb.append(" SET ").append(path(alias,attribute)).append("=");
		if(value!=null) {sb.append("'").append(value).append("'");}
		else {sb.append("NULL");}
		newLine(newLine,sb);
	}
	
	public static <E extends Enum<E>> String sum(String item, E attribute, String as, boolean newLine)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("sum(").append(path(item,attribute)).append(")");
		sb.append(" AS ").append(as);
		newLine(newLine,sb);
		return sb.toString();
	}
	
	public static <E extends Enum<E>> String id(String alias, E attribute)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(path(alias,attribute)).append("_id");
		return sb.toString();
	}
	
	public static <E extends Enum<E>> String path(String alias, E attribute)
	{
		StringBuilder sb = new StringBuilder();
		if(alias!=null && alias.length()>0) {sb.append(alias).append(".");}
		sb.append(attribute.toString());
		return sb.toString();
	}
	
	public static <T extends EjbWithId> void select(StringBuilder sb, Class<T> c, String alias, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append("SELECT id FROM ");
		sb.append(c.getAnnotation(Table.class).name());
		if(alias!=null) {sb.append(" "+alias);}
		newLine(newLine,sb);
	}
	
	public static <T extends EjbWithId> void count(StringBuilder sb, Class<T> c, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append("SELECT COUNT(id) FROM ");
		sb.append(c.getAnnotation(Table.class).name());
		newLine(newLine,sb);
	}
	
	public static <T extends EjbWithId> String deleteFrom(Class<T> c)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(c.getAnnotation(Table.class).name());
		return sb.toString();
	}
	

	
	public static <T extends EjbWithId> void deleteFrom(StringBuilder sb, Class<T> c, String alias, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append("DELETE FROM ");
		sb.append(c.getAnnotation(Table.class).name());
		if(alias!=null) {sb.append(" "+alias);}
		newLine(newLine,sb);
	}
	
	public static <T extends EjbWithId> String delete(T t)
	{
		if(t.getClass().getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(t.getClass().getAnnotation(Table.class).name());
		sb.append(" WHERE id="+t.getId());
		sb.append(";");
		return sb.toString();
	}
	
	public static <T extends EjbWithId> String drop(Class<T> c)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		return drop(c.getAnnotation(Table.class).name());
	}
	
	public static <T extends EjbWithId> String drop(String table)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE ");
		sb.append(table);
		sb.append(";");
		return sb.toString();
	}
	
	public static void from(StringBuilder sb, Class<?> c, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append(" FROM ").append(c.getAnnotation(Table.class).name());
		newLine(newLine,sb);
	}
	
	public static String from(String table, String as, boolean newLine)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("FROM ").append(table).append(" AS ").append(as);
		newLine(newLine,sb);
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> String distinct(StringBuilder sb, String alias, boolean newLine, E... attributes)
	{
		List<String> list = new ArrayList<>();
		for(E attribute : attributes) {list.add(id(alias,attribute));}
		
		sb.append(" DISTINCT ON (");
		sb.append(StringUtils.join(list,","));
		sb.append(")");
		newLine(newLine,sb);
		return sb.toString();
	}
	
	public static <T extends EjbWithId> void from2(StringBuilder sb, Class<T> t, String alias, boolean newLine)
	{
		if(t.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append(" FROM ");
		sb.append(t.getAnnotation(Table.class).name());
		if(alias!=null) {sb.append(" ").append(alias);}
		newLine(newLine,sb);
	}
	
	public static <T extends EjbWithId> void from(StringBuilder sb, Class<T> c, String alias, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append(" FROM ").append(c.getAnnotation(Table.class).name());
		if(Objects.nonNull(alias)) {sb.append(" AS ").append(alias);}
		newLine(newLine,sb);
	}
	
	public static <T extends EjbWithId, E extends Enum<E>> void joinOnwerJ(StringBuilder sb, Class<T> c, String aliasOwner, String aliasOther, E attribute, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append(" JOIN ").append(c.getAnnotation(Table.class).name());
		sb.append(" ").append(aliasOther);
		sb.append(" ON ");
		sb.append(aliasOwner).append(".id = ").append(aliasOther).append(".").append(attribute.toString()).append("_id");
		newLine(newLine,sb);
	}
	public static <T extends EjbWithId, E extends Enum<E>> void joinOnwerOther(StringBuilder sb, Class<T> c, String aliasOwner, String aliasOther, E attribute, boolean newLine)
	{
		if(c.getAnnotation(Table.class)==null) {throw new RuntimeException("Not a @Table)");}
		sb.append(" JOIN ").append(c.getAnnotation(Table.class).name());
		sb.append(" ").append(aliasOther);
		sb.append(" ON ");
		sb.append(aliasOther).append(".id = ").append(aliasOwner).append(".").append(attribute.toString()).append("_id");
		newLine(newLine,sb);
	}
	
	public static <E extends Enum<E>> String inIdList(String item, E attribute, List<EjbWithId> ids, boolean newLine)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(item).append(".").append(attribute).append("_id");
		sb.append(" IN (").append(JeeslSqlQuery.inIdList(ids)).append(")");
		newLine(newLine,sb);
		return sb.toString();
	}
	
	public static <T extends EjbWithId> void valueId(boolean first, StringBuilder sb, T id)
	{
		if(!first) {sb.append(",");}
		sb.append(id.getId());
	}
	public static void valueBool(boolean first, StringBuilder sb, boolean value)
	{
		if(!first) {sb.append(",");}
		if(value) {sb.append("'t'");}
		else {sb.append("'f'");}
	}
	public static void valueInt(boolean first, StringBuilder sb, int value)
	{
		if(!first) {sb.append(",");}
		sb.append(value);
	}
	public static void valueString(boolean first, StringBuilder sb, String value)
	{
		if(!first) {sb.append(",");}
		sb.append("'").append(value).append("'");
	}

	public static <E extends Enum<E>, T extends EjbWithId> void where(StringBuilder sb, String alias, boolean negate, E attribute, T where, boolean newLine)
	{
		sb.append(" WHERE ");
		whereAndOrAttribute(sb,alias,negate,attribute,where,newLine);
	}
	public static <E extends Enum<E>, T extends EjbWithId> void whereId(StringBuilder sb, String alias, T where, boolean newLine)
	{
		sb.append(" WHERE ");
//		sb.append(alias).append(".id=");
		sb.append("id=");
		sb.append(where.getId());
		newLine(newLine,sb);
	}
	public static <E extends Enum<E>, T extends EjbWithId> void whereAnd(StringBuilder sb, String alias, boolean negate, E attribute, T where, boolean newLine)
	{
		sb.append(" AND ");
		whereAndOrAttribute(sb,alias,negate,attribute,where,newLine);
	}
	public static <E extends Enum<E>, T extends EjbWithId> void whereOr(StringBuilder sb, String alias, boolean negate, E attribute, T where, boolean newLine)
	{
		sb.append(" OR ");
		whereAndOrAttribute(sb,alias,negate,attribute,where,newLine);
	}
	private static <E extends Enum<E>, T extends EjbWithId> void whereAndOrAttribute(StringBuilder sb, String alias, boolean negate, E attribute, T where, boolean newLine)
	{
		sb.append(id(alias,attribute));
		if(where!=null)
		{
			if(negate) {sb.append("!=");}
			else {sb.append("=");}
			sb.append(where.getId());
		}
		else
		{
			sb.append(" IS");
			if(negate) {sb.append(" NOT");}
			sb.append(" NULL");
		}
		newLine(newLine,sb);
	}
	
	public static <E extends Enum<E>> void whereAnd(StringBuilder sb, String alias, boolean negate, E attribute, long value, boolean newLine)
	{
		sb.append(" AND ");
		whereAndOrLong(sb,alias,negate,attribute,value,newLine);
	}
	public static <E extends Enum<E>> void whereOr(StringBuilder sb, String alias, boolean negate, E attribute, long value, boolean newLine)
	{
		sb.append(" OR ");
		whereAndOrLong(sb,alias,negate,attribute,value,newLine);
	}
	private static <E extends Enum<E>> void whereAndOrLong(StringBuilder sb, String alias, boolean notNegate, E attribute, long value, boolean newLine)
	{
		if(alias!=null) {sb.append(alias).append(".");}
		sb.append(attribute.toString());
		sb.append("=").append(value);
		newLine(newLine,sb);
	}
	
	public static <T extends EjbWithId> void in(StringBuilder sb, List<T> list)
	{
		List<Long> ids = new ArrayList<>();
		for(T id : list) {ids.add(id.getId());}
		sb.append(" (").append(StringUtils.join(ids,",")).append(")");
	}
	@Deprecated
	public static <T extends EjbWithId> String in(List<T> list)
	{
		List<Long> ids = new ArrayList<>();
		for(T id : list) {ids.add(id.getId());}
		StringBuilder sb = new StringBuilder();
		sb.append(" (").append(StringUtils.join(ids,",")).append(")");
		return sb.toString();
	}
	
	public static void limit(StringBuilder sb, int limit, boolean newLine)
	{
		limit(sb,true,limit,newLine);
	}
	public static void limit(StringBuilder sb, boolean apply, int limit, boolean newLine)
	{
		if(apply)
		{
			sb.append(" LIMIT ").append(limit);
			newLine(newLine,sb);
		}
	}
	
	public static void semicolon(StringBuilder sb) {SqlFactory.semicolon(sb,false);}
	public static void semicolon(StringBuilder sb, boolean newLine)
	{
		sb.append(";");
		newLine(newLine,sb);
	}
	
	public static void newLine(boolean newLine, StringBuilder sb)
	{
		if(newLine){sb.append("\n");}
	}
	
	public static void transactionBegin(StringBuilder sb, boolean newLine)
	{
		sb.append("BEGIN");
		semicolon(sb,newLine);
	}
	public static void transactionCommit(StringBuilder sb, boolean newLine)
	{
		sb.append("COMMIT");
		semicolon(sb,newLine);
	}

	public static Long toLong(Object o)
	{
		if(o instanceof BigInteger) {return ((BigInteger)o).longValue();}
		else if(o instanceof Long) {return (Long)o;}
		else {return null;}
	}
}