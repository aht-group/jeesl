package org.jeesl.model.ejb.io.db;

import java.io.Serializable;
import java.util.Objects;

public class CqBool implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Type {IsValue,NotValue,IsNull}
	
	private final Type type; public final Type getType() {return type;}
	private final Boolean value; public Boolean getValue() {return value;}
	private final String path; public String getPath() {return path;}

	public static CqBool isValue(Boolean value, String path) {return new CqBool(Type.IsValue,value,path);}
	public static CqBool notValue(Boolean value, String path) {return new CqBool(Type.NotValue,value,path);}
	public static CqBool nullValue(String path) {return new CqBool(Type.IsNull,null,path);}

	private CqBool(Type type, Boolean value, String path)
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