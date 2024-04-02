package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCqLong;

public interface JeeslCqLongQuery extends Serializable
{	
	List<JeeslCqLong> getCqLongs();
	
//	void addCqLong(JeeslCqLong c);
}