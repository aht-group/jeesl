package org.jeesl.controller.facade.jx.predicate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.jeesl.controller.facade.jx.io.JeeslIoMavenFacadeBean;
import org.jeesl.model.ejb.io.db.CqLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiteralPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFacadeBean.class);
		
	public static void add(CriteriaBuilder cB, List<Predicate> predicates, CqLiteral lit, Expression<String> eExpression)
	{
		Expression<String> eLiteral = cB.upper(cB.literal("%"+lit.getLiteral()+"%"));
		
		switch(lit.getType())
		{
			case CONTAINS: predicates.add(cB.like(eExpression,eLiteral)); break;
			default: logger.error("NYI Type: "+lit.toString());
		}
	}
}
