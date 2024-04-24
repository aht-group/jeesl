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

public class CqGraphFetch implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(CqGraphFetch.class);
	
	private static final long serialVersionUID = 1L;

	public enum Type {NODE,ATTRIBUTE}
	
	private Type type; public final Type getType() {return type;}
	private Class<?> entity; public Class<?> getEntity() {return entity;}
	private String attribute; public String getAttribute() {return attribute;}

	private List<CqGraphFetch> path; public List<CqGraphFetch> getPath() {return path;}

	public static CqGraphFetch instance() {return new CqGraphFetch();}
	public static CqGraphFetch root(Class<?> entity) {return new CqGraphFetch(entity);}
	
	private CqGraphFetch(Class<?> entity)
	{
		this.entity=entity;
	}
	private CqGraphFetch()
	{
		path = new ArrayList<>();
	}
	private CqGraphFetch(final Type type, final Class<?> entity, final String attribute)
	{
		this.type=type;
		this.entity=entity;
		this.attribute=attribute;
	}
	
	public <T extends Enum<T>> CqGraphFetch node(Class<?> entity, T attribute)
	{
		path.add(new CqGraphFetch(Type.NODE,entity,attribute.toString()));
		return this;
	}
	public <T extends Enum<T>> CqGraphFetch attribute(Class<?> entity, T attribute)
	{
		path.add(new CqGraphFetch(Type.ATTRIBUTE,entity,attribute.toString()));
		return this;
	}
//	public static CqFetch attribute(Serializable...path) {return new CqFetch(Type.ATTRIBUTE,CqFetch.path(path));}

	public static String path(Serializable...attributes)
	{
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<attributes.length;i++)
		{
			sb.append(attributes[i].toString());
			if(i<(attributes.length-1)) {sb.append(".");}
		}
		return sb.toString();
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		if(Objects.nonNull(path))
		{
			for(CqGraphFetch f : path)
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
	
	public static Map<CqGraphFetch,Set<CqGraphFetch>> toMapNode(List<CqGraphFetch> list)
	{
		Map<CqGraphFetch,Set<CqGraphFetch>> map = new HashMap<>();
		for(CqGraphFetch f : list)
		{
			if(f.getPath().size()==1)
			{
				CqGraphFetch.add(Type.NODE,map,CqGraphFetch.root(CqGraphFetch.class),f.getPath().get(0));
			}
			else
			{
				CqGraphFetch.add(Type.NODE,map,CqGraphFetch.root(CqGraphFetch.class),f.getPath().get(0));
				for(int i=0;i<f.getPath().size()-1;i++)
				{
					CqGraphFetch.add(Type.NODE,map,f.getPath().get(i),f.getPath().get(i+1));
				}
			}
		}
		return map;
	}
	public static Map<CqGraphFetch,Set<CqGraphFetch>> toMapAttribute(List<CqGraphFetch> list)
	{
		Map<CqGraphFetch,Set<CqGraphFetch>> map = new HashMap<>();
		for(CqGraphFetch f : list)
		{
			if(f.getPath().size()==1)
			{
				CqGraphFetch.add(Type.ATTRIBUTE,map,CqGraphFetch.root(CqGraphFetch.class),f.getPath().get(0));
			}
			else
			{
				CqGraphFetch.add(Type.ATTRIBUTE,map,CqGraphFetch.root(CqGraphFetch.class),f.getPath().get(0));
				for(int i=0;i<f.getPath().size()-1;i++)
				{
					CqGraphFetch.add(Type.ATTRIBUTE,map,f.getPath().get(i),f.getPath().get(i+1));
				}
			}
		}
		return map;
	}
	
	private static void add(Type type, Map<CqGraphFetch,Set<CqGraphFetch>> map, CqGraphFetch src, CqGraphFetch dst)
	{
		if(type.equals(dst.getType()))
		{
			if(!map.containsKey(src)) {map.put(src,new HashSet<>());}
			map.get(src).add(dst);
		}
	}
	
	@Override public boolean equals(Object object)
	{
		CqGraphFetch o = (CqGraphFetch) object;
		return new EqualsBuilder().append(type,o.getType()).append(entity.getName(),o.getEntity().getName()).append(attribute,o.getAttribute()).isEquals();
	}
	@Override public int hashCode() {return new HashCodeBuilder(27,33).append(type).append(entity.getName()).append(attribute).toHashCode();}
}