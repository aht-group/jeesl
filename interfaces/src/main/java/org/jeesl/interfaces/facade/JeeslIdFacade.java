package org.jeesl.interfaces.facade;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

//jeesl.highlight:interface
public interface JeeslIdFacade extends Serializable
{
	<T extends Object> List<T> all(Class<T> type);
	<T extends Object> List<T> all(Class<T> type, int maxResults);
	
	<T extends EjbWithId> List<T> list(Class<T> c, List<Long> list);
	List<Long> listId(String nativeQuery);
	
	<T extends Object> T find(Class<T> type, long id) throws JeeslNotFoundException;
	<T extends EjbWithId> T find(Class<T> type, T t);
	<T extends EjbWithId> List<T> find(Class<T> c, List<Long> ids);
	<T extends EjbWithId> List<T> find(Class<T> c, Set<Long> ids);
	<T extends EjbWithId> long maxId(Class<T> c);
	
	<T extends EjbWithId>T loadEntityWithGraph(Class<T> clazz, String graphName, long parentId);
}
//jeesl.highlight:interface