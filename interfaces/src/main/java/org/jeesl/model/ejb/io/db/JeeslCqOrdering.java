package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public interface JeeslCqOrdering extends Serializable
{
	public enum SortOrder {ASCENDING,DESCENDING,UNSORTED}
	
	SortOrder getOrder();
	String getPath();
}