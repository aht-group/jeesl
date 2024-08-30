package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public interface JeeslCqLiteral extends Serializable
{
	public enum Type {STARTS,CONTAINS,EXACT}
	
	Type getType();
	String getLiteral();
	String getPath();
	
	String nyi(Class<?> c);
}