package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCqDouble;

public interface JeeslCqDoubleQuery extends Serializable
{	
	List<JeeslCqDouble> getCqDouble();
	
	void addCqDouble(JeeslCqDouble c);
}