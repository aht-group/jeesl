package org.jeesl.interfaces.util.query.cq;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.ejb.io.db.JeeslCq;

public interface JeeslCqAggregationQuery extends Serializable
{	
	List<JeeslCq.Agg> getCqAggregations();
	
	void addCqAggregation(JeeslCq.Agg cq);
}