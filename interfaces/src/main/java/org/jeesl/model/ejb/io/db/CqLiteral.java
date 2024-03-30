package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public class CqLiteral implements JeeslCqLiteral
{
	private static final long serialVersionUID = 1L;
	
	private final Type type; @Override public Type getType() {return type;}
	private final String literal; @Override public String getLiteral() {return literal;}
	private final String path; @Override public String getPath() {return path;}

	public static CqLiteral starts(String literal, String path) {return new CqLiteral(Type.STARTS,literal,path);}
	public static CqLiteral contains(String literal, String path) {return new CqLiteral(Type.CONTAINS,literal,path);}

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
	
	public static String path(Serializable...attributes) {return CqOrdering.path(attributes);}
	public static String pathMl(Serializable...attributes) {return CqOrdering.path(attributes)+"[localeCode].lang";}
}