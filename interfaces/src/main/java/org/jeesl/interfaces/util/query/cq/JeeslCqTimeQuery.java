package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCqTime;

public interface JeeslCqTimeQuery extends Serializable
{	
	List<JeeslCqTime> getCqTimes();
	
	void addCqTime(JeeslCqTime c);
}