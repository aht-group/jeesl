package org.jeesl.util.query.cq;

import java.io.Serializable;
import java.util.Objects;

import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqDouble;

public class CqDouble implements JeeslCqDouble
{
	private static final long serialVersionUID = 1L;

	private final JeeslCqDouble.Type type; public final JeeslCqDouble.Type getType() {return type;}
	private final Double value; @Override public Double getValue() {return value;}
	private final String path; public String getPath() {return path;}

	public static CqDouble isValue(int value, String path) {return CqDouble.isValue(Integer.valueOf(value).doubleValue(),path);}
	public static CqDouble isValue(double value, String path) {return new CqDouble(JeeslCqDouble.Type.IsValue,value,path);}
	
	public static CqDouble greaterThan(int value, String path) {return CqDouble.greaterThan(Integer.valueOf(value).doubleValue(),path);}
	public static CqDouble greaterThan(double value, String path) {return new CqDouble(JeeslCqDouble.Type.GreaterThan,value,path);}
	
	public static CqDouble lessThan(int value, String path) {return CqDouble.lessThan(Integer.valueOf(value).doubleValue(),path);}
	public static CqDouble lessThan(double value, String path) {return new CqDouble(JeeslCqDouble.Type.LessThan,value,path);}
	
	public static CqDouble notValue(Double value, String path) {return new CqDouble(JeeslCqDouble.Type.NotValue,value,path);}
	public static CqDouble nullValue(String path) {return new CqDouble(JeeslCqDouble.Type.IsNull,null,path);}
	public static CqDouble nonNull(String path) {return new CqDouble(JeeslCqDouble.Type.NonNull,null,path);}

	private CqDouble(JeeslCqDouble.Type type, Double value, String path)
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