package org.jeesl.model.ejb.io.db;

import java.io.Serializable;
import java.time.LocalDate;

public class CqDate implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Type {DbIsBefore,DbIsBeforeOrEqual,DbIsEqual,DbIsEqualOrAfter,DbIsAfter}
	
	private final Type type; public final Type getType() {return type;}
	private final LocalDate date; public LocalDate getDate() {return date;}
	private final String path; public String getPath() {return path;}

	public static CqDate dbIsBeforeOrEqual(LocalDate date, String path) {return new CqDate(Type.DbIsBeforeOrEqual,date,path);}
	public static CqDate dbIsEqualOrAfter(LocalDate date, String path) {return new CqDate(Type.DbIsEqualOrAfter,date,path);}
	public static CqDate dbIsEqual(LocalDate date, String path) {return new CqDate(Type.DbIsEqual,date,path);}

	private CqDate(Type type, LocalDate date, String path)
	{
		this.type=type;
		this.date=date;
		this.path=path;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(path);
		sb.append(" ").append(type.toString());
		sb.append(" ").append(date.toString());
		
		return sb.toString();
	}
	
	public static String path(Serializable...attributes) {return CqOrdering.path(attributes);}
}