package org.jeesl.controller.facade.jx.predicate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiteralPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(LiteralPredicateBuilder.class);
		
	public static void add(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqLiteral cq, Expression<String> expression)
	{
		Expression<String> literal;
		switch(cq.getType())
		{
			case STARTS: 	literal = cB.upper(cB.literal(cq.getLiteral()+"%"));
							predicates.add(cB.like(cB.upper(expression),literal));
							break;
			case CONTAINS: 	literal = cB.upper(cB.literal("%"+cq.getLiteral()+"%"));
							predicates.add(cB.like(cB.upper(expression),literal));
							break;
			case EXACT: 	literal = cB.literal(cq.getLiteral());
							predicates.add(cB.equal(expression,literal));
							break;
			case NULL: 		predicates.add(cB.isNull(expression));
							break;
			case NONNULL: 	predicates.add(cB.isNotNull(expression)); break;
			case NotEmpty:	predicates.add(cB.and(cB.isNotNull(expression),cB.notEqual(expression,""))); break;
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
	
	public static <T extends EjbWithId, L extends JeeslLang, E extends Enum<E>> void ml(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqLiteral cq, Root<T> root, E attribute, String localeCode)
	{
		MapJoin<T,String,L> lang = root.joinMap(attribute.toString());
		predicates.add(cB.equal(lang.key(),localeCode));
		
		Expression<String> e = lang.get(JeeslLang.Att.lang.toString());
		LiteralPredicateBuilder.add(cB,predicates,cq,e);
	}
	public static <O extends EjbWithId, T extends EjbWithId, L extends JeeslLang, E extends Enum<E>> void ml(CriteriaBuilder cB, List<Predicate> predicates, JeeslCqLiteral cq, Join<O,T> join, E attribute, String localeCode)
	{
		MapJoin<T,String,L> lang = join.joinMap(attribute.toString());
		predicates.add(cB.equal(lang.key(),localeCode));
		
		Expression<String> e = lang.get(JeeslLang.Att.lang.toString());
		LiteralPredicateBuilder.add(cB,predicates,cq,e);
	}
}