package org.jeesl.model.ejb.io.db;

import java.io.Serializable;
import java.util.List;

public interface JeeslCqLong extends Serializable
{
	public enum Type {IsValue,IsNull,IsNonNull}
	
	Long getValue();
	List<Long> getValues();
	Type getType();
	String getPath();
	
	String nyi(Class<?> c);
}