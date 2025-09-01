package org.jeesl.util.query.cq;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqDate;

public class CqDate implements JeeslCqDate
{
	private static final long serialVersionUID = 1L;

	private final Type type; public final Type getType() {return type;}
	private final LocalDate date; public LocalDate getDate() {return date;}
	private final String path; public String getPath() {return path;}

	public static CqDate lessThan(LocalDate date) {return new CqDate(Type.Before,date,null);}
	
	public static CqDate isBefore(LocalDate date, String path) {return new CqDate(Type.Before,date,path);}
	public static CqDate isBeforeOrAt(LocalDate date, String path) {return new CqDate(Type.BeforeOrAt,date,path);}
	public static CqDate isAt(LocalDate date, String path) {return new CqDate(Type.Equal,date,path);}
	public static CqDate isAtOrAfter(LocalDate date, String path) {return new CqDate(Type.AtOrAfter,date,path);}
	public static CqDate isNull(String path) {return new CqDate(Type.Null,null,path);}
	public static CqDate notNull(String path) {return new CqDate(Type.NotNull,null,path);}
	
	private CqDate(Type type, LocalDate date, String path)
	{
		this.type=type;
		this.date=date;
		this.path=path;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(path);
		sb.append(" ").append(type.toString());
		sb.append(" ").append(Objects.nonNull(date) ? date.toString() : "--");
		
		return sb.toString();
	}
	
	public String nyi(Class<?> c)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("NYI ");
		sb.append(this.toString());
		sb.append(" ").append(c.getName());
		return sb.toString();
	}
	
	public static String path(Serializable...attributes) {return CqGraphFetch.path(attributes);}
}