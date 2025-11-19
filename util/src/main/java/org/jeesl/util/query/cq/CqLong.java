package org.jeesl.util.query.cq;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.math.NumberUtils;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.model.ejb.io.db.JeeslCqLong;

public class CqLong implements JeeslCqLong
{
	private static final long serialVersionUID = 1L;

	private final Type type; @Override public final Type getType() {return type;}
	private Long id; @Override public Long getValue() {return id;}
	private List<Long> values; @Override public List<Long> getValues() {return values;}
	private final String path; @Override public String getPath() {return path;}

	public static CqLong isValue(JeeslCqLiteral cq) {return new CqLong(Type.IsValue,Long.valueOf(cq.getLiteral()),cq.getPath());}
//	public static CqLong isValue(Integer id, String path) {return new CqLong(Type.IsValue,id,path);}
	public static CqLong isValue(long id, String path) {return new CqLong(Type.IsValue,id,path);}
	public static CqLong in(List<Long> values, String path) {return new CqLong(Type.IsValue,values,path);}
	
	public static CqLong value(Long id, String path)
	{
		if(Objects.isNull(id)) {return new CqLong(Type.IsNull,id,path);}
		else {return new CqLong(Type.IsValue,id,path);}
	}
	public static <T extends EjbWithId> CqLong value(T t, String path)
	{
		if(Objects.isNull(t)) {return new CqLong(Type.IsNull,path);}
		else {return new CqLong(Type.IsValue,t.getId(),path);}
	}
	public static CqLong empty(String path) {return new CqLong(Type.IsNull,path);}

	private CqLong(Type type, String path)
	{
		this.type=type;
		this.path=path;
	}
	private CqLong(Type type, Long id, String path)
	{
		this.type=type;
		this.id=id;
		this.path=path;
	}
	private CqLong(Type type, List<Long> values, String path)
	{
		this.type=type;
		this.values=values;
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
	
	public static boolean isParseable(JeeslCqLiteral c)
	{
		return NumberUtils.isParsable(c.getLiteral());
	}
	
	public String nyi(Class<?> c)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("NYI ");
		sb.append(this.toString());
		sb.append(" ").append(c.getName());
		return sb.toString();
	}
}