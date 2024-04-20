package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCqRootFetch;

public interface JeeslCqRootFetchQuery extends Serializable
{	
	List<JeeslCqRootFetch> getCqRootFetches();
	
	void addCqRootFetch(JeeslCqRootFetch c);
}