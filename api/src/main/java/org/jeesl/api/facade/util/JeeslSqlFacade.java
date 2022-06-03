package org.jeesl.api.facade.util;

import java.io.Serializable;
import java.util.List;

public interface JeeslSqlFacade extends Serializable
{
	List<Long> nativeQueryForIds(String query);
	List<String> nativeQueryForString(String query);
	List<Object> nativeQuery(String sql);
	List<Object> jpaQuery(String sql);
}