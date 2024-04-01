package org.jeesl.controller.facade.jx.predicate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.jeesl.controller.facade.jx.io.JeeslIoMavenFacadeBean;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiteralPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFacadeBean.class);
		
	public static void add(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqLiteral cq, Expression<String> expression)
	{
		Expression<String> literal = cB.upper(cB.literal("%"+cq.getLiteral()+"%"));
		
		switch(cq.getType())
		{
			case CONTAINS: predicates.add(cB.like(cB.upper(expression),literal)); break;
			default: logger.error("NYI Type: "+cq.toString());
		}
	}
	
	public static void or(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqLiteral cq, Expression<String> e1, Expression<String> e2)
	{
		Expression<String> literal = cB.upper(cB.literal("%"+cq.getLiteral()+"%"));

		switch(cq.getType())
		{
			case CONTAINS: predicates.add(cB.or(cB.like(cB.upper(e1),literal),cB.like(cB.upper(e2),literal))); break;
			default: logger.error("NYI Type: "+cq.toString());
		}
	}
}