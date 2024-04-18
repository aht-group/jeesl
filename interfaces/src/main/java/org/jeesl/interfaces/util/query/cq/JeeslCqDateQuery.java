package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCqDate;

public interface JeeslCqDateQuery extends Serializable
{	
	List<JeeslCqDate> getCqDates();
	
	void addCqDate(JeeslCqDate c);
}