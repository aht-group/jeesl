package org.jeesl.util.query.cq;

import java.io.Serializable;
import java.util.Objects;

import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqInteger;

public class CqInteger implements JeeslCqInteger
{
	private static final long serialVersionUID = 1L;

	private final JeeslCqInteger.Type type; public final JeeslCqInteger.Type getType() {return type;}
	private final Integer value; public Integer getValue() {return value;}
	private final String path; public String getPath() {return path;}

	public static CqInteger isValue(Integer value, String path) {return new CqInteger(JeeslCqInteger.Type.IsValue,value,path);}
	public static CqInteger notValue(Integer value, String path) {return new CqInteger(JeeslCqInteger.Type.NotValue,value,path);}
	public static CqInteger isNull(String path) {return new CqInteger(JeeslCqInteger.Type.IsNull,null,path);}
	public static CqInteger nonNull(String path) {return new CqInteger(JeeslCqInteger.Type.NonNull,null,path);}
	
	public static CqInteger lessOrEqual(Integer value, String path) {return new CqInteger(JeeslCqInteger.Type.LessOrEqual,value,path);}
	public static CqInteger greaterOrEqual(Integer value, String path) {return new CqInteger(JeeslCqInteger.Type.GreaterOrEqual,value,path);}

	private CqInteger(JeeslCqInteger.Type type, Integer value, String path)
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
	
	public static String path(Serializable...attributes) {return CqGraphFetch.path(attributes);}
	
	public String nyi(Class<?> c)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("NYI ");
		sb.append(this.toString());
		sb.append(" ").append(c.getName());
		return sb.toString();
	}
}