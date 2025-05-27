package org.jeesl.util.query.cq;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqTime;

public class CqTime implements JeeslCqTime
{
	private static final long serialVersionUID = 1L;

	private final Type type; public final Type getType() {return type;}
	private final LocalDateTime time; public LocalDateTime getTime() {return time;}

	private final String path; public String getPath() {return path;}

	public static CqTime equalTo(LocalDateTime time, String path) {return new CqTime(Type.equalTo,time,path);}
	public static CqTime lessThan(LocalDateTime time, String path) {return new CqTime(Type.lessThan,time,path);}
	public static CqTime greaterThan(LocalDateTime time, String path) {return new CqTime(Type.greaterThan,time,path);}
	
	public static CqTime leDb(LocalDateTime date, String path) {return new CqTime(Type.leDb,date,path);}
	public static CqTime isNull(String path) {return new CqTime(Type.Null,null,path);}
//	public static CqTime dbIsEqualOrAfter(LocalDate date, String path) {return new CqTime(Type.DbIsEqualOrAfter,date,path);}
//	public static CqTime dbIsEqual(LocalDate date, String path) {return new CqTime(Type.DbIsEqual,date,path);}

	private CqTime(Type type, LocalDateTime time, String path)
	{
		this.type=type;
		this.time=time;
		this.path=path;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" ").append(path);
		sb.append(" ").append(type.toString());
		sb.append(" ").append(Objects.nonNull(time) ? time.toString() : "--");
		
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