package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCqEntity;

public interface JeeslCqEntityQuery extends Serializable
{	
	List<JeeslCqEntity> getCqEntities();
	
	void addCqEntity(JeeslCqEntity c);
}