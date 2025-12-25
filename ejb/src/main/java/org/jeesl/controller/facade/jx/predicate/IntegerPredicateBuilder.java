package org.jeesl.controller.facade.jx.predicate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.jeesl.controller.facade.jx.io.JeeslIoMavenFacadeBean;
import org.jeesl.model.ejb.io.db.JeeslCqInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFacadeBean.class);
		
	public static void add(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqInteger c, Expression<Integer> eInteger)
	{
		switch(c.getType())
		{
			case IsValue: predicates.add(cB.equal(eInteger,c.getValue())); break;
			case IsNull: predicates.add(cB.isNull(eInteger)); break;
			case NonNull: predicates.add(cB.isNotNull(eInteger)); break;
			case LessOrEqual: predicates.add(cB.lessThanOrEqualTo(eInteger,c.getValue())); break;
			case GreaterOrEqual: predicates.add(cB.greaterThanOrEqualTo(eInteger,c.getValue())); break;
			
			default: logger.error("NYI Type: "+c.toString());
		}
	}
}