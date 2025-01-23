package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public interface JeeslCqInteger extends Serializable
{
	public enum Type {IsValue,NotValue,IsNull,NonNull}
	
	Integer getValue();
	Type getType();
	String getPath();
	
	String nyi(Class<?> c);
}