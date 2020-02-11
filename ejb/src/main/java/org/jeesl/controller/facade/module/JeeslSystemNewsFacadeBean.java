package org.jeesl.controller.facade.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.system.JeeslSystemNewsFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.news.JeeslSystemNews;
import org.jeesl.interfaces.model.with.date.EjbWithValidFrom;
import org.jeesl.interfaces.model.with.date.EjbWithValidUntil;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public class JeeslSystemNewsFacadeBean<L extends JeeslLang,D extends JeeslDescription,
										CATEGORY extends JeeslStatus<CATEGORY,L,D>,
										NEWS extends JeeslSystemNews<L,D,CATEGORY,NEWS,USER>,
										USER extends EjbWithId>
					extends JeeslFacadeBean
					implements JeeslSystemNewsFacade<L,D,CATEGORY,NEWS,USER>
{	
	final static Logger logger = LoggerFactory.getLogger(JeeslSystemNewsFacadeBean.class);
	
	private final Class<NEWS> cNews;
	
	public JeeslSystemNewsFacadeBean(EntityManager em, final Class<NEWS> cNews)
	{
		super(em);
		this.cNews=cNews;
	}

	@Override
	public List<NEWS> fActiveNews()
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<NEWS> cQ = cB.createQuery(cNews);
		Root<NEWS> news = cQ.from(cNews);
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Path<Boolean> pathVisible = news.get(JeeslSystemNews.Attributes.visible.toString());
		Expression<Date> dStart = news.get(EjbWithValidFrom.Attributes.validFrom.toString());
		Expression<Date> dEnd   = news.get(EjbWithValidUntil.Attributes.validUntil.toString());
		
		LocalDate date = new LocalDateTime().toLocalDate();
		predicates.add(cB.isTrue(pathVisible));
		predicates.add(cB.lessThanOrEqualTo(dStart,date.toDate()));
		predicates.add(cB.greaterThanOrEqualTo(dEnd,date.toDate()));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(news);
		cQ.orderBy(cB.desc(dStart));

		TypedQuery<NEWS> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
}