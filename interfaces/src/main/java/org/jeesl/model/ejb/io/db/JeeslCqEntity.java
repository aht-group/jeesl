package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslCqEntity extends Serializable
{
	public enum Type {IsNull,IsNonNull}
	
	EjbWithId getValue();
	Type getType();
	String getPath();
	
	String nyi(Class<?> c);
}