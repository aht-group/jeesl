package org.jeesl.controller.facade.jx.predicate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.exlp.util.system.DateUtil;
import org.jeesl.model.ejb.io.db.JeeslCqTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimePredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(TimePredicateBuilder.class);
	
	public static void juDate(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqTime cq, Expression<Date> eDate)
	{
		switch(cq.getType())
		{
			case equalTo: predicates.add(cB.equal(eDate,DateUtil.toDate(cq.getTime()))); break;
			case lessThan: predicates.add(cB.lessThan(eDate,DateUtil.toDate(cq.getTime()))); break;
			case greaterThan: predicates.add(cB.greaterThan(eDate,DateUtil.toDate(cq.getTime()))); break;
			
			case leDb: predicates.add(cB.lessThanOrEqualTo(eDate,DateUtil.toDate(cq.getTime()))); break;
			default: logger.warn("NYI "+cq.toString()); break;
		}
	}
	
	public static void jtTime(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqTime cq, Expression<LocalDateTime> e)
	{
//		logger.info("Applying "+cq.toString()+" "+predicates.size());
		switch(cq.getType())
		{
			case equalTo: predicates.add(cB.equal(e,cq.getTime())); break;
			case lessThan: predicates.add(cB.lessThan(e,cq.getTime())); break;
			case greaterThan: predicates.add(cB.greaterThan(e,cq.getTime())); break;
			
			case leDb: predicates.add(cB.lessThanOrEqualTo(e,cq.getTime())); break;
			case Null: predicates.add(cB.isNull(e)); break;
			default: logger.warn("NYI "+cq.toString()); break;
		}
//		logger.info("Done "+cq.toString()+" "+predicates.size());
	}
}