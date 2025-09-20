package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public interface JeeslCqDouble extends Serializable
{
	public enum Type {IsNull,NonNull,IsValue,NotValue,GreaterThan,LessThan}
	
	Double getValue();
	Type getType();
	String getPath();
	
	String nyi(Class<?> c);
}