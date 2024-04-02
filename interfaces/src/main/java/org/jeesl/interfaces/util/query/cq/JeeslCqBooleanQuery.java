package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCqBoolean;

public interface JeeslCqBooleanQuery extends Serializable
{	
	List<JeeslCqBoolean> getCqBooleans();
	
	void addCqBoolean(JeeslCqBoolean c);
}