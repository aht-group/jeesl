package org.jeesl.controller.facade.jx.predicate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.jeesl.controller.facade.jx.io.JeeslIoMavenFacadeBean;
import org.jeesl.model.ejb.io.db.JeeslCqDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoublePredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFacadeBean.class);
		
	public static void add(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqDouble cq, Expression<Double> eDouble)
	{
		switch(cq.getType())
		{
			case NonNull: predicates.add(cB.isNotNull(eDouble)); break;
			case IsValue: predicates.add(cB.equal(eDouble,cq.getValue())); break;
			case GreaterThan: predicates.add(cB.greaterThan(eDouble,cq.getValue())); break;
			case GreaterOrEqual: predicates.add(cB.greaterThanOrEqualTo(eDouble,cq.getValue())); break;
			case LessThan: predicates.add(cB.lessThan(eDouble,cq.getValue())); break;
			case LessOrEqual: predicates.add(cB.lessThanOrEqualTo(eDouble,cq.getValue())); break;
			default: logger.error("NYI Type: "+cq.toString());
		}
	}
}