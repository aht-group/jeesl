package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public interface JeeslCqRootFetch extends Serializable
{
	public enum Type {LEFT}
	
	Type getType();
	String getPath();
	
//	void x();
}