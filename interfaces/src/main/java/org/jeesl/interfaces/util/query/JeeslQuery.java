package org.jeesl.interfaces.util.query;

import java.io.Serializable;

public interface JeeslQuery extends Serializable
{
	public String getLocaleCode();
	
	void reset();
	
	boolean isTupleLoad();
	void setTupleLoad(boolean tupleLoad);
	
	boolean isDistinct();
	void setDistinct(boolean distinct);

	Integer getFirstResult();
	void setFirstResult(Integer firstResult);

	Integer getMaxResults();
	void setMaxResults(Integer maxResults);
	
	String getSortBy();

	boolean isSortAscending();
	
	void sort(String sortBy, boolean sortAscending);
	void noSort();
	boolean withSort();
	
	void debug(boolean debug);
	void debug(boolean debug, int ident);
	
	
	String getString1();
	void setString1(String string1);
	
	String getString2();
	void setString2(String string2);
	
	String getString3();
	void setString3(String string3);
}