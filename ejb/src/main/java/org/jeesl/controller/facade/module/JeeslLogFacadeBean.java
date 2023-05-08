package org.jeesl.controller.facade.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.JeeslLogFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.LogFactoryBuilder;
import org.jeesl.interfaces.model.module.diary.JeeslLogBook;
import org.jeesl.interfaces.model.module.diary.JeeslLogConfidentiality;
import org.jeesl.interfaces.model.module.diary.JeeslLogImpact;
import org.jeesl.interfaces.model.module.diary.JeeslLogItem;
import org.jeesl.interfaces.model.module.diary.JeeslLogScope;
import org.jeesl.interfaces.model.module.diary.JeeslWithDiary;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

public class JeeslLogFacadeBean<L extends JeeslLang, D extends JeeslDescription,
									LOG extends JeeslLogBook<SCOPE,ITEM>,
									SCOPE extends JeeslLogScope<L,D,SCOPE,?>,
									ITEM extends JeeslLogItem<L,D,?,?,LOG,IMPACT,CONF,USER>,
									IMPACT extends JeeslLogImpact<L,D,IMPACT,?>,
									CONF extends JeeslLogConfidentiality<L,D,CONF,?>,
									USER extends EjbWithId
									>
					extends JeeslFacadeBean
					implements JeeslLogFacade<L,D,LOG,SCOPE,ITEM,IMPACT,CONF,USER>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslLogFacadeBean.class);
	
	private final LogFactoryBuilder<L,D,LOG,SCOPE,ITEM,IMPACT,CONF,USER> fbLog;
	
	public JeeslLogFacadeBean(EntityManager em, final LogFactoryBuilder<L,D,LOG,SCOPE,ITEM,IMPACT,CONF,USER> fbLog)
	{
		super(em);
		this.fbLog=fbLog;
	}
	
	public <OWNER extends JeeslWithDiary<LOG>> OWNER fDiaryOwner(Class<OWNER> cOwner, LOG diary) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<OWNER> cQ = cB.createQuery(cOwner);
		Root<OWNER> owner = cQ.from(cOwner);
        
        Path<LOG> pDiary = owner.get(JeeslWithDiary.Attributes.log.toString());   
        CriteriaQuery<OWNER> select = cQ.select(owner);
	    select.where(cB.equal(pDiary,diary));

		TypedQuery<OWNER> q = em.createQuery(cQ);
		try	{return q.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+cOwner.getSimpleName()+" found for code:"+owner.toString());}
	}
	
	@Override
	public List<ITEM> fLogItems(List<LOG> logs)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ITEM> cQ = cB.createQuery(fbLog.getClassItem());
		Root<ITEM> item = cQ.from(fbLog.getClassItem());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<ITEM,LOG> jLog = item.join(JeeslLogItem.Attributes.log.toString());
		Path<Date> pRecord = item.get(JeeslLogItem.Attributes.record.toString());
		
		predicates.add(jLog.in(logs));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.asc(pRecord));
		cQ.select(item);

		TypedQuery<ITEM> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}

	@Override public List<ITEM> fLogItems(List<LOG> books, List<SCOPE> scopes, List<CONF> confidentialities, LocalDate startDate, LocalDate endDate)
	{
		if(books!=null && books.isEmpty()) {return new ArrayList<ITEM>();}
		if(scopes!=null && scopes.isEmpty()) {return new ArrayList<ITEM>();}
		if(confidentialities.isEmpty()) {return new ArrayList<ITEM>();}
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ITEM> cQ = cB.createQuery(fbLog.getClassItem());
		Root<ITEM> item = cQ.from(fbLog.getClassItem());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<ITEM,LOG> jBook = item.join(JeeslLogItem.Attributes.log.toString());
		if(books!=null)
		{
			predicates.add(jBook.in(books));
		}
		
		if(scopes!=null)
		{
			Join<LOG,SCOPE> jScope = jBook.join(JeeslLogBook.Attributes.scope.toString());
			predicates.add(jScope.in(scopes));
		}
		
		if(ObjectUtils.isNotEmpty(confidentialities))
		{
			ListJoin<ITEM,CONF> jConf = item.joinList(JeeslLogItem.Attributes.confidentialities.toString());
			predicates.add(jConf.in(confidentialities));
		}
		
		Expression<Date> eRecord = item.get(JeeslLogItem.Attributes.record.toString());
		if(Objects.nonNull(startDate))
		{
			predicates.add(cB.greaterThan(eRecord,DateUtil.toDate(startDate.atStartOfDay())));
		}
		if(Objects.nonNull(endDate))
		{
			predicates.add(cB.lessThan(eRecord,DateUtil.toDate(endDate.atStartOfDay().plusDays(1))));
		}
		
		cQ.select(item).distinct(true);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));		

		TypedQuery<ITEM> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
}