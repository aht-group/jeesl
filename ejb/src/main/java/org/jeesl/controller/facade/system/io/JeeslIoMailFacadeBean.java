package org.jeesl.controller.facade.system.io;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.io.JeeslIoMailFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoMailFactoryBuilder;
import org.jeesl.factory.ejb.system.io.mail.EjbIoMailFactory;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.model.system.io.mail.core.JeeslMailRetention;
import org.jeesl.interfaces.model.system.io.mail.core.JeeslMailStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMailFacadeBean<L extends JeeslLang,D extends JeeslDescription,
									CATEGORY extends JeeslStatus<CATEGORY,L,D>,
									MAIL extends JeeslIoMail<L,D,CATEGORY,STATUS,RETENTION,FRC>,
									STATUS extends JeeslMailStatus<L,D,STATUS,?>,
									RETENTION extends JeeslMailRetention<L,D,RETENTION,?>,
									FRC extends JeeslFileContainer<?,?>>
					extends JeeslFacadeBean
					implements JeeslIoMailFacade<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslIoMailFacadeBean.class);
		
	private final IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC> fbMail;
	
	private EjbIoMailFactory<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC> efMail;
	
	public JeeslIoMailFacadeBean(EntityManager em, IoMailFactoryBuilder<L,D,CATEGORY,MAIL,STATUS,RETENTION,FRC> fbMail)
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
			STATUS statusSpooling = fByCode(fbMail.getClassStatus(), JeeslMailStatus.Code.spooling);
			STATUS statusQueue = fByCode(fbMail.getClassStatus(), JeeslMailStatus.Code.queue);
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
	
	@Override public void queueMail(CATEGORY category, RETENTION retention, Mail mail) throws JeeslConstraintViolationException, JeeslNotFoundException
	{
		STATUS status = this.fByCode(fbMail.getClassStatus(), JeeslMailStatus.Code.queue);
		if(retention==null)
		{
			retention = this.fByCode(fbMail.getClassRetention(), JeeslIoMail.Retention.fully);	
		}
		MAIL ejb = efMail.build(category,status,mail,retention);
		ejb = this.persist(ejb);
		logger.info(fbMail.getClassMail().getSimpleName()+" spooled with id="+ejb.getId());
	}

	@Override public List<MAIL> fSpoolMails(int maxResult)
	{
		List<MAIL> mails = new ArrayList<MAIL>();
		try
		{
			STATUS statusSpooling = fByCode(fbMail.getClassStatus(), JeeslMailStatus.Code.spooling);
			STATUS statusQueue = fByCode(fbMail.getClassStatus(), JeeslMailStatus.Code.queue);
			
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
		
		DateTime dt = new DateTime();
		predicates.add(cB.equal(pStatus,status));
		predicates.add(cB.or(cB.isNull(pRecordSpool),cB.lessThanOrEqualTo(pRecordSpool, dt.minusMinutes(5).toDate())));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.desc(pRecordCreation));
		cQ.select(mail);

		TypedQuery<MAIL> tQ = em.createQuery(cQ);
		tQ.setMaxResults(maxResult);
		
		return tQ.getResultList();
	}
}