package org.jeesl.model.ejb.io.db;

import java.io.Serializable;
import java.util.Objects;

public class CqInteger implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Type {IsValue,NotValue,IsNull}
	
	private final Type type; public final Type getType() {return type;}
	private final Integer value; public Integer getValue() {return value;}
	private final String path; public String getPath() {return path;}

	public static CqInteger isValue(Integer value, String path) {return new CqInteger(Type.IsValue,value,path);}
	public static CqInteger notValue(Integer value, String path) {return new CqInteger(Type.NotValue,value,path);}
	public static CqInteger nullValue(String path) {return new CqInteger(Type.IsNull,null,path);}

	private CqInteger(Type type, Integer value, String path)
	{
		this.type=type;
		this.value=value;
		this.path=path;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(path);
		sb.append(" ").append(type.toString());
		if(Objects.nonNull(value)) {sb.append(" ").append(value.toString());}
		
		return sb.toString();
	}
	
	public static String path(Serializable...attributes) {return CqOrdering.path(attributes);}
}