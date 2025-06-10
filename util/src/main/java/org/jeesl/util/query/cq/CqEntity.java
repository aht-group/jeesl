package org.jeesl.util.query.cq;

import java.io.Serializable;
import java.util.Objects;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqEntity;

public class CqEntity implements JeeslCqEntity
{
	private static final long serialVersionUID = 1L;

	private final Type type; @Override public final Type getType() {return type;}
	private final EjbWithId id; @Override public EjbWithId getValue() {return id;}
	private final String path; @Override public String getPath() {return path;}

	public static CqEntity isNull(String path) {return new CqEntity(Type.IsNull,null,path);}
	public static CqEntity nonNull(String path) {return new CqEntity(Type.IsNonNull,null,path);}
	public static CqEntity includeEmpty(String path) {return new CqEntity(Type.IncludeEmpty,null,path);}

	private CqEntity(Type type, EjbWithId id, String path)
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
		if(Objects.nonNull(id)) {sb.append(" ").append(id.toString());}
		
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