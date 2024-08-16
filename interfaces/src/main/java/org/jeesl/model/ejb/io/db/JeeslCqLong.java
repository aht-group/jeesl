package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public interface JeeslCqLong extends Serializable
{
	public enum Type {IsValue,IsNull,IsNonNull}
	
	Long getValue();
	Type getType();
	String getPath();
	
	String nyi(Class<?> c);
}