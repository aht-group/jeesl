package org.jeesl.api.facade.util;

import java.io.Serializable;
import java.util.List;

public interface JeeslSqlFacade extends Serializable
{
	List<Object> nativeQuery(String sql);
	Object nativeQueryForObject(String sql);
	List<Long> nativeQueryForIds(String query);
	
	List<String> nativeQueryForString(String query);
	
	List<Object> jpaQuery(String sql);
}