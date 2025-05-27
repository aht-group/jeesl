package org.jeesl.model.ejb.io.db;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface JeeslCqTime extends Serializable
{
	public enum Type {Null,equalTo,greaterThan,lessThan,leDb}
	
	Type getType();
	LocalDateTime getTime();
	String getPath();
	
	String nyi(Class<?> c);
}