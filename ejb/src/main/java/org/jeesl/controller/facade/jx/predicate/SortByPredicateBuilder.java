package org.jeesl.controller.facade.jx.predicate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;

import org.jeesl.controller.facade.jx.io.JeeslIoMavenFacadeBean;
import org.jeesl.model.ejb.io.db.JeeslCqOrdering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SortByPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFacadeBean.class);
	
	public static void juDate(CriteriaBuilder cB, List<Order> orders, JeeslCqOrdering sortBy, Expression<Date> e)
	{
		if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.ASCENDING) {orders.add(cB.asc(e));}
		else if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.DESCENDING) {orders.add(cB.desc(e));}
	}
	
	public static void jtDate(CriteriaBuilder cB, List<Order> orders, JeeslCqOrdering sortBy, Expression<LocalDate> e)
	{
		if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.ASCENDING) {orders.add(cB.asc(e));}
		else if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.DESCENDING) {orders.add(cB.desc(e));}
	}
	
	public static void jtDateTime(CriteriaBuilder cB, List<Order> orders, JeeslCqOrdering sortBy, Expression<LocalDateTime> e)
	{
		if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.ASCENDING) {orders.add(cB.asc(e));}
		else if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.DESCENDING) {orders.add(cB.desc(e));}
	}
	
	public static void addByInteger(CriteriaBuilder cB, List<Order> orders, JeeslCqOrdering cq, Expression<Integer> eInteger)
	{
		if(cq.getOrder()==JeeslCqOrdering.SortOrder.ASCENDING) {orders.add(cB.asc(eInteger));}
		else if(cq.getOrder()==JeeslCqOrdering.SortOrder.DESCENDING) {orders.add(cB.desc(eInteger));}
	}
	
	public static void addByLong(CriteriaBuilder cB, List<Order> orders, JeeslCqOrdering sortBy, Expression<Long> eLong)
	{
		if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.ASCENDING) {orders.add(cB.asc(eLong));}
		else if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.DESCENDING) {orders.add(cB.desc(eLong));}
	}
	
	public static void addByString(CriteriaBuilder cB, List<Order> orders, JeeslCqOrdering sortBy, Expression<String> eString)
	{
		if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.ASCENDING) {orders.add(cB.asc(eString));}
		else if(sortBy.getOrder()==JeeslCqOrdering.SortOrder.DESCENDING) {orders.add(cB.desc(eString));}
	}
}
