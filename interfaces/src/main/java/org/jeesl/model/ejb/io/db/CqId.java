package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public class CqId implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Type {IsValue,IsNull,IsNonNull}
	
	private final Type type; public final Type getType() {return type;}
	private final Long id; public Long getId() {return id;}
	private final String path; public String getPath() {return path;}

	public static CqId isValue(Long id, String path) {return new CqId(Type.IsValue,id,path);}
//	public static CqId dbIsEqualOrAfter(LocalDate date, String path) {return new CqId(Type.DbIsEqualOrAfter,date,path);}
//	public static CqId dbIsEqual(LocalDate date, String path) {return new CqId(Type.DbIsEqual,date,path);}

	private CqId(Type type, Long id, String path)
	{
		this.type=type;
		this.id=id;
		this.path=path;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(path);
		sb.append(" ").append(type.toString());
		sb.append(" ").append(id.toString());
		
		return sb.toString();
	}
	
	public static String path(Serializable...attributes) {return CqOrdering.path(attributes);}
}