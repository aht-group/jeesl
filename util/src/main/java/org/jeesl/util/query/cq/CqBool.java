package org.jeesl.util.query.cq;

import java.io.Serializable;
import java.util.Objects;

import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqBoolean;

public class CqBool implements JeeslCqBoolean
{
	private static final long serialVersionUID = 1L;

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