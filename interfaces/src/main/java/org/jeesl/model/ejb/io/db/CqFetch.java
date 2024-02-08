package org.jeesl.model.ejb.io.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CqFetch implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(CqFetch.class);
	
	private static final long serialVersionUID = 1L;

	public enum Type {NODE,ATTRIBUTE}
	
	private Type type; public final Type getType() {return type;}
	private Class<?> entity; public Class<?> getEntity() {return entity;}
	private String attribute; public String getAttribute() {return attribute;}

	private List<CqFetch> path; public List<CqFetch> getPath() {return path;}

	public static CqFetch instance() {return new CqFetch();}
	public static CqFetch root(Class<?> entity) {return new CqFetch(entity);}
	
	private CqFetch(Class<?> entity)
	{
		this.entity=entity;
	}
	private CqFetch()
	{
		path = new ArrayList<>();
	}
	private CqFetch(final Type type, final Class<?> entity, final String attribute)
	{
		this.type=type;
		this.entity=entity;
		this.attribute=attribute;
	}
	
	public <T extends Enum<T>> CqFetch node(Class<?> entity, T attribute)
	{
		path.add(new CqFetch(Type.NODE,entity,attribute.toString()));
		return this;
	}
	public <T extends Enum<T>> CqFetch attribute(Class<?> entity, T attribute)
	{
		path.add(new CqFetch(Type.ATTRIBUTE,entity,attribute.toString()));
		return this;
	}
//	public static CqFetch attribute(Serializable...path) {return new CqFetch(Type.ATTRIBUTE,CqFetch.path(path));}

//	private static String path(Serializable...attributes)
//	{
//		StringBuffer sb = new StringBuffer();
//		
//		for(int i=0;i<attributes.length;i++)
//		{
//			sb.append(attributes[i].toString());
//			if(i<(attributes.length-1)) {sb.append(".");}
//		}
//		return sb.toString();
//	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		if(Objects.nonNull(path))
		{
			for(CqFetch f : path)
			{
				sb.append(f.toString());
				sb.append(" ");
			}
		}
		else
		{
			if(Objects.nonNull(type)) {sb.append(type).append(":");}
			sb.append(entity.getSimpleName());
			if(Objects.nonNull(attribute)) {sb.append(":").append(attribute);}
		}
		return sb.toString().trim();
	}
	
	public static Map<CqFetch,Set<CqFetch>> toMapNode(List<CqFetch> list)
	{
		Map<CqFetch,Set<CqFetch>> map = new HashMap<>();
		for(CqFetch f : list)
		{
			if(f.getPath().size()==1)
			{
				CqFetch.add(Type.NODE,map,CqFetch.root(CqFetch.class),f.getPath().get(0));
			}
			else
			{
				CqFetch.add(Type.NODE,map,CqFetch.root(CqFetch.class),f.getPath().get(0));
				for(int i=0;i<f.getPath().size()-1;i++)
				{
					CqFetch.add(Type.NODE,map,f.getPath().get(i),f.getPath().get(i+1));
				}
			}
		}
		return map;
	}
	public static Map<CqFetch,Set<CqFetch>> toMapAttribute(List<CqFetch> list)
	{
		Map<CqFetch,Set<CqFetch>> map = new HashMap<>();
		for(CqFetch f : list)
		{
			if(f.getPath().size()==1)
			{
				CqFetch.add(Type.ATTRIBUTE,map,CqFetch.root(CqFetch.class),f.getPath().get(0));
			}
			else
			{
				CqFetch.add(Type.ATTRIBUTE,map,CqFetch.root(CqFetch.class),f.getPath().get(0));
				for(int i=0;i<f.getPath().size()-1;i++)
				{
					CqFetch.add(Type.ATTRIBUTE,map,f.getPath().get(i),f.getPath().get(i+1));
				}
			}
		}
		return map;
	}
	
	private static void add(Type type, Map<CqFetch,Set<CqFetch>> map, CqFetch src, CqFetch dst)
	{
		if(type.equals(dst.getType()))
		{
			if(!map.containsKey(src)) {map.put(src,new HashSet<>());}
			map.get(src).add(dst);
		}
	}
	
	@Override public boolean equals(Object object)
	{
		CqFetch o = (CqFetch) object;
		return new EqualsBuilder().append(type,o.getType()).append(entity.getName(),o.getEntity().getName()).append(attribute,o.getAttribute()).isEquals();
	}
	@Override public int hashCode() {return new HashCodeBuilder(27,33).append(type).append(entity.getName()).append(attribute).toHashCode();}
}