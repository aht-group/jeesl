package org.jeesl.util.query.cq;

import java.io.Serializable;

import org.jeesl.model.ejb.io.db.CqOrdering;
import org.jeesl.model.ejb.io.db.JeeslCqRootFetch;

public class CqRootFetch implements JeeslCqRootFetch
{
	private static final long serialVersionUID = 1L;
	
	private final Type type; @Override public Type getType() {return type;}
	private final String path; @Override public String getPath() {return path;}

	public static CqRootFetch left(String literal, String path) {return new CqRootFetch(Type.LEFT,path);}
//	public static CqRootFetch contains(String literal, String path) {return new CqRootFetch(Type.CONTAINS,literal,path);}
//	public static CqRootFetch exact(String literal, String path) {return new CqRootFetch(Type.EXACT,literal,path);}

	private CqRootFetch(Type type, String path)
	{
		this.type=type;
		this.path=path;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(type.toString());
		sb.append(" ");
		sb.append(" in [").append(path).append("]");
		return sb.toString();
	}
	
	public static String path(Serializable...attributes) {return CqOrdering.path(attributes);}
}