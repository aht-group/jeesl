package org.jeesl.model.ejb.io.db;

import java.io.Serializable;
import java.time.LocalDate;

public class CqDate implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Type {Before,BeforeOrEqual,Equal,AfterOrEqual,After}
	
	private final Type type; public final Type getType() {return type;}
	private final LocalDate date; public LocalDate getDate() {return date;}

	public static CqDate beforeOrEqual(LocalDate date) {return new CqDate(Type.BeforeOrEqual,date);}
	public static CqDate afterOrEqual(LocalDate date) {return new CqDate(Type.AfterOrEqual,date);}

	private CqDate(Type type, LocalDate date)
	{
		this.type=type;
		this.date=date;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(type.toString());
		sb.append(" ");
		sb.append(date.toString());
		return sb.toString();
	}
}