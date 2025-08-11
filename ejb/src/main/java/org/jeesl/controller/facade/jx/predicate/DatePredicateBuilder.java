package org.jeesl.controller.facade.jx.predicate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.exlp.util.system.DateUtil;
import org.jeesl.model.ejb.io.db.JeeslCqDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatePredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(DatePredicateBuilder.class);
	
	public static void juDate(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqDate cq, Expression<Date> eDate)
	{
		switch(cq.getType())
		{
			case AtOrAfter: predicates.add(cB.greaterThanOrEqualTo(eDate,DateUtil.toDate(cq.getDate().atStartOfDay()))); break;
			case BeforeOrAt: predicates.add(cB.lessThanOrEqualTo(eDate,DateUtil.toDate(cq.getDate().plusDays(1).atStartOfDay()))); break;
			case Before: predicates.add(cB.lessThan(eDate,DateUtil.toDate(cq.getDate().atStartOfDay()))); break;
			default: logger.warn("NYI "+cq.toString()); break;
		}
	}
	
	public static void add(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqDate cq, Expression<LocalDate> eDate)
	{
		switch(cq.getType())
		{
			case AtOrAfter: predicates.add(cB.greaterThanOrEqualTo(eDate,cq.getDate())); break;
			case BeforeOrAt: predicates.add(cB.lessThanOrEqualTo(eDate,cq.getDate())); break;
			case Before: predicates.add(cB.lessThan(eDate,cq.getDate())); break;
			case Equal: predicates.add(cB.equal(eDate,cq.getDate())); break;
			case Null: predicates.add(cB.isNull(eDate)); break;
			default: logger.warn("NYI "+cq.toString()); break;
		}
	}
	
	public static void time(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqDate cq, Expression<LocalDateTime> eTime)
	{
		switch(cq.getType())
		{
			case AtOrAfter: predicates.add(cB.greaterThanOrEqualTo(eTime,cq.getDate().atStartOfDay())); break;
			case BeforeOrAt: predicates.add(cB.lessThan(eTime,cq.getDate().plusDays(1).atStartOfDay())); break;
			case Null: predicates.add(cB.isNull(eTime)); break;
			default: logger.warn("NYI "+cq.toString()); break;
		}
	}
}