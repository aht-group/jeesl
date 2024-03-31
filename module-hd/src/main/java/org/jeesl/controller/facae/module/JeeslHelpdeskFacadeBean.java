package org.jeesl.controller.facae.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslHdFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.hd.JeeslHdCategory;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEvent;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEventType;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFaq;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFga;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdLevel;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdMessage;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdPriority;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdScope;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicket;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicketStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslHelpdeskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslHelpdeskFacadeBean<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										R extends JeeslTenantRealm<L,D,R,?>,
										CAT extends JeeslHdCategory<L,D,R,CAT,?>,
										TICKET extends JeeslHdTicket<R,EVENT,M,?>,
										
										STATUS extends JeeslHdTicketStatus<L,D,R,STATUS,?>,
										EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
										TYPE extends JeeslHdEventType<L,D,TYPE,?>,
										LEVEL extends JeeslHdLevel<L,D,R,LEVEL,?>,
										PRIORITY extends JeeslHdPriority<L,D,R,PRIORITY,?>,
										MSG extends JeeslHdMessage<TICKET,M,SCOPE,USER>,
										M extends JeeslIoMarkup<MT>,
										MT extends JeeslIoMarkupType<L,D,MT,?>,
										FAQ extends JeeslHdFaq<L,D,R,CAT,SCOPE>,
										SCOPE extends JeeslHdScope<L,D,SCOPE,?>,
										FGA extends JeeslHdFga<FAQ,DOC,SEC>,
										DOC extends JeeslIoCms<L,D,LOC,?,SEC>,
										SEC extends JeeslIoCmsSection<L,SEC>,
										USER extends JeeslSimpleUser>
					extends JeeslFacadeBean
					implements JeeslHdFacade<L,D,R,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,USER>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslHelpdeskFacadeBean.class);
	
	private final HdFactoryBuilder<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,?,USER> fbHd;
	
	public JeeslHelpdeskFacadeBean(EntityManager em, final HdFactoryBuilder<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,?,USER> fbHd)
	{
		super(em);
		this.fbHd=fbHd;
	}

	@Override public TICKET saveHdTicket(TICKET ticket, EVENT event, USER initiator) throws JeeslConstraintViolationException, JeeslLockingException
	{
		boolean unsaved = EjbIdFactory.isUnSaved(ticket);
		
		if(unsaved)
		{
			ticket = this.save(ticket);
			TYPE type = this.fByEnum(fbHd.getClassType(),JeeslHdEventType.Code.create);
			event.getTypes().add(type);
			event = this.save(event);
			ticket.setFirstEvent(event);
			ticket.setLastEvent(event);
			ticket = this.save(ticket);
		}
		else
		{
			List<TYPE> types = new ArrayList<>();
			try
			{
				EVENT original = this.find(fbHd.getClassEvent(),event.getId());
				
				if(!original.getCategory().equals(event.getCategory())) {types.add(this.fByEnum(fbHd.getClassType(),JeeslHdEventType.Code.category));}
				if(!original.getStatus().equals(event.getStatus())) {types.add(this.fByEnum(fbHd.getClassType(),JeeslHdEventType.Code.status));}
				if(!original.getLevel().equals(event.getLevel())) {types.add(this.fByEnum(fbHd.getClassType(),JeeslHdEventType.Code.level));}
				if(!original.getSupporterPriority().equals(event.getSupporterPriority())) {types.add(this.fByEnum(fbHd.getClassType(),JeeslHdEventType.Code.priority));}
				if(!EjbIdFactory.equals(original.getSupporter(),event.getSupporter())) {types.add(this.fByEnum(fbHd.getClassType(),JeeslHdEventType.Code.supporter));}
				if(!EjbIdFactory.equals(original.getReporter(),event.getReporter())) {types.add(this.fByEnum(fbHd.getClassType(),JeeslHdEventType.Code.reporter));}
			}
			catch (JeeslNotFoundException e){}
			
			if(!types.isEmpty())
			{
				ticket.setLastEvent(null);
				ticket = this.save(ticket);
				event.setId(0);
				event.setInitiator(initiator);
				event.setRecord(new Date());
				event.setTypes(types);
				event = save(event);
				ticket.setLastEvent(event);
				ticket = this.save(ticket);
			}	
		}
		
		return ticket;
	}

	@Override public <RREF extends EjbWithId> List<TICKET> fHdTickets(JeeslHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TICKET> cQ = cB.createQuery(fbHd.getClassTicket());
		Root<TICKET> root = cQ.from(fbHd.getClassTicket());
		
		Join<TICKET,EVENT> jLastEvent = root.join(JeeslHdTicket.Attributes.lastEvent.toString());
		
		if(query.getReporters()!=null)
		{
			Path<USER> pReporter = jLastEvent.get(JeeslHdEvent.Attributes.reporter.toString());
			if(query.getReporters().isEmpty()) {predicates.add(cB.isNull(pReporter));}
			else{predicates.add(pReporter.in(query.getReporters()));}
		}
		
		if(query.getStatus()!=null)
		{
			Path<STATUS> pStatus = jLastEvent.get(JeeslHdEvent.Attributes.status.toString());
			if(query.getStatus().isEmpty()) {predicates.add(cB.isNull(pStatus));}
			else{predicates.add(pStatus.in(query.getStatus()));}
		}
//		Expression<Date> dStart = root.get(JeeslCalendarItem.Attributes.startDate.toString());
//		Expression<Date> dEnd   = root.get(JeeslCalendarItem.Attributes.endDate.toString());
//		
//		//After
//	    Predicate startAfterFrom = cB.greaterThanOrEqualTo(dStart, from);
//	    Predicate endAfterFrom = cB.greaterThanOrEqualTo(dEnd, from);
//	    Predicate endAfterTo = cB.greaterThanOrEqualTo(dEnd, to);
//	    
//	    //Before
//	    Predicate startBeforeTo = cB.lessThan(dStart, to);
//	    Predicate startBeforeFrom = cB.lessThan(dStart, from);
//	    Predicate endBeforeTo = cB.lessThan(dEnd, to);
//		
//		Predicate pOnlyStartAndStartInRange = cB.and(cB.isNull(dEnd),startAfterFrom,startBeforeTo);
//		Predicate pStartAndEndInRange = cB.and(cB.isNotNull(dEnd),startAfterFrom,endBeforeTo);
//		Predicate pStartOutsideEndInRange = cB.and(cB.isNotNull(dEnd),startBeforeFrom,endAfterFrom,endBeforeTo);
//		Predicate pStartInRangeEndOutside = cB.and(cB.isNotNull(dEnd),startAfterFrom,startBeforeTo,endAfterTo);
//		Predicate pStartBeforeRangeEndAfterRange = cB.and(cB.isNotNull(dEnd),startBeforeFrom,endAfterTo);
//		predicates.add(cB.or(pOnlyStartAndStartInRange,pStartAndEndInRange,pStartOutsideEndInRange,pStartInRangeEndOutside,pStartBeforeRangeEndAfterRange));
//		
//		Join<ITEM,CALENDAR> jCalendar = root.join(JeeslCalendarItem.Attributes.calendar.toString());
//		predicates.add(cB.isTrue(jCalendar.in(calendars)));
				    
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);
//		cQ.orderBy(cB.asc(dStart));
		
		return em.createQuery(cQ).getResultList();
	}
}