package org.jeesl.controller.facade.jx.predicate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.jeesl.controller.facade.jx.io.JeeslIoMavenFacadeBean;
import org.jeesl.model.ejb.io.db.CqId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFacadeBean.class);
		
	public static void add(CriteriaBuilder cB, List<Predicate> predicates, CqId c, Expression<Long> e)
	{
		switch(c.getType())
		{
			case IsValue: predicates.add(cB.equal(e,c.getValue())); break;
			default: logger.error("NYI Type: "+c.toString());
		}
	}
}