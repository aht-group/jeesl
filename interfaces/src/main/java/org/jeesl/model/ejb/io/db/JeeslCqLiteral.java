package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public interface JeeslCqLiteral extends Serializable
{
	public enum Type {STARTS,CONTAINS}
	
	Type getType();
	String getLiteral();
	String getPath();
}