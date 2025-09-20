package org.jeesl.controller.facade.jx.predicate;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.model.ejb.io.db.JeeslCqLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(LongPredicateBuilder.class);
		
	public static void add(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqLong c, Expression<Long> e)
	{
		switch(c.getType())
		{
			case IsValue:	if(Objects.nonNull(c.getValue()))
							{
								logger.trace(c.getType()+" "+c.getValue()+" "+c.getPath());
								predicates.add(cB.equal(e,c.getValue()));
							}
							if(ObjectUtils.isNotEmpty(c.getValues()))
							{
								logger.trace(c.getType()+" "+c.getValues()+" "+c.getPath());
								predicates.add(e.in(c.getValues()));
							}
							break;
			case IsNull: predicates.add(cB.isNull(e)); break;
			default: logger.error("NYI Type: "+c.toString());
		}
	}
}