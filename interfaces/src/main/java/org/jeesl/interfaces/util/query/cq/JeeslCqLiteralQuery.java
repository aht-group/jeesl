package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.primefaces.model.FilterMeta;

public interface JeeslCqLiteralQuery extends Serializable
{	
	List<JeeslCqLiteral> getCqLiterals();
	
	void addCqLiteral(JeeslCqLiteral c);
	
//	void applyPrimefaces(Map<String,FilterMeta> filterBy);
}