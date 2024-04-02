package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public interface JeeslCqBoolean extends Serializable
{
	public enum Type {IsValue,NotValue,IsNull}
	
	Boolean getValue();
	Type getType();
	String getPath();
}