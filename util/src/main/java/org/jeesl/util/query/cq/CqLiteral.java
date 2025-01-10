package org.jeesl.util.query.cq;

import java.io.Serializable;

import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;

public class CqLiteral implements JeeslCqLiteral
{
	private static final long serialVersionUID = 1L;
	
	private final Type type; @Override public Type getType() {return type;}
	private final String literal; @Override public String getLiteral() {return literal;}
	private final String path; @Override public String getPath() {return path;}

	public static CqLiteral starts(String literal, String path) {return new CqLiteral(Type.STARTS,literal,path);}
	public static CqLiteral contains(String literal, String path) {return new CqLiteral(Type.CONTAINS,literal,path);}
	
	public static <E extends Enum<E>> CqLiteral exact(E literal, String path) {return CqLiteral.exact(literal.toString(), path);}
	public static CqLiteral exact(String literal, String path) {return new CqLiteral(Type.EXACT,literal,path);}
	
	public static CqLiteral isNull(String path) {return new CqLiteral(Type.NULL,null,path);}
	public static CqLiteral isNotNull(String path) {return new CqLiteral(Type.NONNULL,null,path);}
	public static CqLiteral isNotEmpty(String path) {return new CqLiteral(Type.NotEmpty,null,path);}
	
	private CqLiteral(Type type, String literal, String path)
	{
		this.type=type;
		this.literal=literal;
		this.path=path;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(type.toString());
		sb.append(" ");
		sb.append("'").append(literal).append("'");
		sb.append(" in [").append(path).append("]");
		return sb.toString();
	}
	
	public static String path(Serializable...attributes) {return CqGraphFetch.path(attributes);}
	public static String pathMl(Serializable...attributes) {return CqGraphFetch.path(attributes)+"[localeCode].lang";}
	
	public String nyi(Class<?> c)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("NYI ");
		sb.append(this.toString());
		sb.append(" ").append(c.getName());
		return sb.toString();
	}
}