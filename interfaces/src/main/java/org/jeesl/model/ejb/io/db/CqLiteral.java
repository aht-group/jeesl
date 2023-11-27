package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public class CqLiteral implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Type {STARTS,LIKE}
	
	private final Type type; public final Type getType() {return type;}
	private String literal; public String getLiteral() {return literal;}

	public static CqLiteral starts(String literal) {return new CqLiteral(Type.STARTS,literal);}

	private CqLiteral(Type type, String literal)
	{
		this.type=type;
		this.literal=literal;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(type.toString());
		sb.append(" ");
		sb.append(literal);
		return sb.toString();
	}
}