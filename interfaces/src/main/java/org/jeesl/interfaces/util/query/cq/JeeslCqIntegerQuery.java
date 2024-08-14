package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCqInteger;

public interface JeeslCqIntegerQuery extends Serializable
{	
	List<JeeslCqInteger> getCqInteger();
	
	void addCqInteger(JeeslCqInteger c);
}