package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCqLiteral;

public interface JeeslCqLiteralQuery extends Serializable
{	
	List<JeeslCqLiteral> getCqLiterals();
	
	void addCqLiteral(JeeslCqLiteral c);
}