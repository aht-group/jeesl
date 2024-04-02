package org.jeesl.model.ejb.io.db;

import java.io.Serializable;
import java.util.Objects;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public class CqId implements JeeslCqLong
{
	private static final long serialVersionUID = 1L;

	private final Type type; @Override public final Type getType() {return type;}
	private final Long id; @Override public Long getValue() {return id;}
	private final String path; @Override public String getPath() {return path;}

//	public static CqId isValue(CqLiteral cq) {return new CqId(Type.IsValue,id,path);}
	public static CqId isValue(Long id, String path) {return new CqId(Type.IsValue,id,path);}
	public static CqId value(Long id, String path)
	{
		if(Objects.isNull(id)) {return new CqId(Type.IsNull,id,path);}
		else {return new CqId(Type.IsValue,id,path);}
	}
	public static <T extends EjbWithId> CqId value(T t, String path)
	{
		if(Objects.isNull(t)) {return new CqId(Type.IsNull,null,path);}
		else {return new CqId(Type.IsValue,t.getId(),path);}
	}
	public static CqId empty(String path) {return new CqId(Type.IsNull,null,path);}

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
		if(Objects.nonNull(id)) {sb.append(" ").append(id.toString());}
		
		return sb.toString();
	}
	
	public static String path(Serializable...attributes) {return CqOrdering.path(attributes);}
}