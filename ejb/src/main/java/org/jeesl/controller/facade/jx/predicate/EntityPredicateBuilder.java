package org.jeesl.controller.facade.jx.predicate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.JeeslCqEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(EntityPredicateBuilder.class);
		
	public static <T extends EjbWithId> void add(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqEntity cq, Path<T> e)
	{
		switch(cq.getType())
		{
			case IsNull: predicates.add(e.isNull()); break;
			case IsNonNull: predicates.add(e.isNotNull()); break;
			default: logger.error("NYI Type: "+cq.toString());
		}
	}
	
	public static <T extends EjbWithId> void join(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqEntity cq, Join<T,?> j)
	{
		switch(cq.getType())
		{
			case IsNull: predicates.add(j.isNull()); break;
			case IsNonNull: predicates.add(j.isNotNull()); break;
			default: logger.error("NYI Type: "+cq.toString());
		}
	}
}