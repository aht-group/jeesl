package org.jeesl.controller.facade.jx.io;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.exlp.util.system.DateUtil;
import org.jeesl.api.facade.io.JeeslIoMailFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoMailFactoryBuilder;
import org.jeesl.factory.ejb.io.mail.core.EjbIoMailFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailRetention;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.ejb.io.db.JeeslCq;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.xml.io.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMailFacadeBean<CATEGORY extends JeeslStatus<?,?,CATEGORY>,
									MAIL extends JeeslIoMail<?,?,CATEGORY,STATUS,RETENTION,FRC>,
									STATUS extends JeeslIoMailStatus<?,?,STATUS,?>,
									RETENTION extends JeeslIoMailRetention<?,?,RETENTION,?>,
									FRC extends JeeslFileContainer<?,?>>
					extends JeeslFacadeBean
					implements JeeslIoMailFacade<CATEGORY,MAIL,STATUS,RETENTION,FRC>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslIoMailFacadeBean.class);
		
	private final IoMailFactoryBuilder<?,?,CATEGORY,MAIL,STATUS,RETENTION> fbMail;
	
	private EjbIoMailFactory<CATEGORY,MAIL,STATUS,RETENTION> efMail;
	
	public JeeslIoMailFacadeBean(EntityManager em, IoMailFactoryBuilder<?,?,CATEGORY,MAIL,STATUS,RETENTION> fbMail)
	{
		super(em);
		this.fbMail = fbMail;
		
		efMail = fbMail.mail();
	}
	
	@Override public Integer cQueue()
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Long> cQ = cB.createQuery(Long.class);
		Root<MAIL> mail = cQ.from(fbMail.getClassMail());
		
		Path<STATUS> pStatus = mail.get(JeeslIoMail.Attributes.status.toString());

		try
		{
			STATUS statusSpooling = fByCode(fbMail.getClassStatus(), JeeslIoMailStatus.Code.spooling);
			STATUS statusQueue = fByCode(fbMail.getClassStatus(), JeeslIoMailStatus.Code.queue);
			cQ.where(cB.or(cB.equal(pStatus,statusSpooling),cB.equal(pStatus,statusQueue)));
			cQ.select(cB.count(mail));
			TypedQuery<Long> tQ = em.createQuery(cQ);
			Long c = tQ.getSingleResult();
			return c.intValue();
		}
		catch (JeeslNotFoundException e)
		{
			logger.error(e.getMessage());
			return null;
		}
	}
	
	@Override public List<MAIL> fMails(List<CATEGORY> categories, List<STATUS> status, List<RETENTION> retentions, Date from, Date to, Integer maxResult)
	{
		if(categories==null || categories.isEmpty()){return new ArrayList<MAIL>();}
		if(status==null || status.isEmpty()){return new ArrayList<MAIL>();}
		if(retentions==null || retentions.isEmpty()){return new ArrayList<MAIL>();}
		
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<MAIL> cQ = cB.createQuery(fbMail.getClassMail());
		Root<MAIL> mail = cQ.from(fbMail.getClassMail());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Path<Date> pRecordCreation = mail.get(JeeslIoMail.Attributes.recordCreation.toString());
		Path<CATEGORY> pCategory = mail.get(JeeslIoMail.Attributes.category.toString());
		Path<STATUS> pStatus = mail.get(JeeslIoMail.Attributes.status.toString());
		Path<RETENTION> pRetention = mail.get(JeeslIoMail.Attributes.retention.toString());
		
		if(from!=null){predicates.add(cB.greaterThanOrEqualTo(pRecordCreation, from));}
		if(to!=null){predicates.add(cB.lessThan(pRecordCreation,to));}
		
		predicates.add(pCategory.in(categories));
		predicates.add(pStatus.in(status));
		predicates.add(pRetention.in(retentions));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.desc(pRecordCreation));
		cQ.select(mail);

		TypedQuery<MAIL> tQ = em.createQuery(cQ);
		if(maxResult!=null) {tQ.setMaxResults(maxResult);}
		return tQ.getResultList();
	}
	
	@Override public void queueMail(CATEGORY category, RETENTION retention, Mail mail) throws JeeslConstraintViolationException
	{
		STATUS status = this.fByEnum(fbMail.getClassStatus(), JeeslIoMailStatus.Code.queue);
		if(retention==null) {retention = this.fByEnum(fbMail.getClassRetention(), JeeslIoMailRetention.Code.fully);}
		MAIL ejb = efMail.build(category,status,mail,retention);
		ejb = this.persist(ejb);
		logger.info(fbMail.getClassMail().getSimpleName()+" spooled with id="+ejb.getId());
	}

	@Override public List<MAIL> fSpoolMails(int maxResult)
	{
		List<MAIL> mails = new ArrayList<MAIL>();
		try
		{
			STATUS statusSpooling = fByCode(fbMail.getClassStatus(), JeeslIoMailStatus.Code.spooling);
			STATUS statusQueue = fByCode(fbMail.getClassStatus(), JeeslIoMailStatus.Code.queue);
			
			mails.addAll(fMails(statusSpooling,maxResult));
			
			if(mails.size()<maxResult)
			{
				mails.addAll(fMails(statusQueue,maxResult-mails.size()));
			}
		}
		catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
		
		return mails;
	}
	
	public List<MAIL> fMails(STATUS status, int maxResult)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<MAIL> cQ = cB.createQuery(fbMail.getClassMail());
		Root<MAIL> mail = cQ.from(fbMail.getClassMail());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Path<STATUS> pStatus = mail.get(JeeslIoMail.Attributes.status.toString());
		Path<Date> pRecordCreation = mail.get(JeeslIoMail.Attributes.recordCreation.toString());
		Path<Date> pRecordSpool = mail.get(JeeslIoMail.Attributes.recordSpool.toString());
		
		LocalDateTime ldt = LocalDateTime.now().minusMinutes(5);
		predicates.add(cB.equal(pStatus,status));
		predicates.add(cB.or(cB.isNull(pRecordSpool),cB.lessThanOrEqualTo(pRecordSpool, DateUtil.toDate(ldt))));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.desc(pRecordCreation));
		cQ.select(mail);

		TypedQuery<MAIL> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		
		return tQ.getResultList();
	}
	
	@Override public JsonTuples1<STATUS> tpcIoMailByStatus(LocalDate from, LocalDate to, List<CATEGORY> categories)
	{		
		Json1TuplesFactory<STATUS> jtf = Json1TuplesFactory.instance(fbMail.getClassStatus()).tupleLoad(this,true);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<MAIL> item = cQ.from(fbMail.getClassMail());
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<STATUS> pStatus = item.get(JeeslIoMail.Attributes.status.toString());
				
		List<Predicate> predicates = new ArrayList<Predicate>();
		Expression<Date> eRecord = item.get(JeeslIoMail.Attributes.recordCreation.toString());
		if(from!=null) {predicates.add(cB.greaterThanOrEqualTo(eRecord,DateUtil.toDate(from)));}
		if(to!=null){predicates.add(cB.lessThan(eRecord,DateUtil.toDate(to.plusDays(1))));}
		if(categories!=null)
		{
			Path<CATEGORY> pCategory = item.get(JeeslIoMail.Attributes.category.toString());
			if(categories.isEmpty()) {predicates.add(cB.isNull(pCategory));}
			else {predicates.add(pCategory.in(categories));}
		}
		
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.groupBy(pStatus.get("id"));
		cQ.multiselect(pStatus.get("id"),eCount);
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.buildV2(tQ.getResultList(),JeeslCq.Agg.count);
	}
}