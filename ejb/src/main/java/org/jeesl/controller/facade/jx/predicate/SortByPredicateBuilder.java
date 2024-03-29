package org.jeesl.controller.facade.jx.predicate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;

import org.jeesl.controller.facade.jx.io.JeeslIoMavenFacadeBean;
import org.jeesl.model.ejb.io.db.CqOrdering;
import org.jeesl.model.ejb.io.db.CqOrdering.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SortByPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFacadeBean.class);
		
	public static void addByInteger(CriteriaBuilder cB, List<Order> orders, CqOrdering sortBy, Expression<Integer> eInteger)
	{
		if(sortBy.getOrder()==SortOrder.ASCENDING) {orders.add(cB.asc(eInteger));}
		else if(sortBy.getOrder()==SortOrder.DESCENDING) {orders.add(cB.desc(eInteger));}
	}
	
	public static void addByLong(CriteriaBuilder cB, List<Order> orders, CqOrdering sortBy, Expression<Long> eLong)
	{
		if(sortBy.getOrder()==SortOrder.ASCENDING) {orders.add(cB.asc(eLong));}
		else if(sortBy.getOrder()==SortOrder.DESCENDING) {orders.add(cB.desc(eLong));}
	}
	
	public static void addByString(CriteriaBuilder cB, List<Order> orders, CqOrdering sortBy, Expression<String> eString)
	{
		if(sortBy.getOrder()==SortOrder.ASCENDING) {orders.add(cB.asc(eString));}
		else if(sortBy.getOrder()==SortOrder.DESCENDING) {orders.add(cB.desc(eString));}
	}
}
