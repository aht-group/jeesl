package org.jeesl.controller.facade.module;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.CalendarFactoryBuilder;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.jeesl.interfaces.model.module.calendar.JeeslWithCalendar;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCalendarFacadeBean<L extends JeeslLang, D extends JeeslDescription,
									CALENDAR extends JeeslCalendar<ZONE,CT>,
									ZONE extends JeeslCalendarTimeZone<L,D>,
									CT extends JeeslCalendarType<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT>,
									IT extends JeeslCalendarItemType<L,D,?,IT,?>
									>
					extends JeeslFacadeBean
					implements JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslCalendarFacadeBean.class);

	
	private final CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar;
	
	public JeeslCalendarFacadeBean(EntityManager em, CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar)
	{
		super(em);
		this.fbCalendar=fbCalendar;
	}
	
	@Override public <OWNER extends JeeslWithCalendar<CALENDAR>> CALENDAR fCalendar(Class<OWNER> cOwner, OWNER owner) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CALENDAR> cQ = cB.createQuery(fbCalendar.getClassCalendar());
		Root<OWNER> root = cQ.from(cOwner);
		
		Path<CALENDAR> pathCalendar = root.get("calendar");
		Path<Long> pId = root.get("id");
		
		cQ.where(cB.equal(pId,owner.getId()));
		cQ.select(pathCalendar);
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No Graphic found for status.id"+owner);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple Results for status.id"+owner);}
	}
	
	@Override public <OWNER extends JeeslWithCalendar<CALENDAR>> OWNER fCalendarOwner(Class<OWNER> cOwner, CALENDAR calendar) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<OWNER> cQ = cB.createQuery(cOwner);
		Root<OWNER> root = cQ.from(cOwner);
		
		Path<CALENDAR> pathCalendar = root.get("calendar");
		
		cQ.where(cB.equal(pathCalendar,calendar));
		cQ.select(root);
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+cOwner.getSimpleName()+" found for calendar="+calendar);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple Results for status.id"+calendar);}
	}

	@Override public List<ITEM> fCalendarItems(CALENDAR calendar, LocalDateTime from, LocalDateTime to)
	{
		List<CALENDAR> calendars = new ArrayList<CALENDAR>();
		calendars.add(calendar);
		return fCalendarItems(calendars,from,to);
	}
	
	@Override
	public List<ITEM> fCalendarItems(List<CALENDAR> calendars, LocalDateTime startLocalDate, LocalDateTime endLocalDate)
	{
		
//		Date start = Date.from(startLocalDate.atZone(ZoneId.systemDefault()).toInstant());
//		Date end = Date.from(endLocalDate.atZone(ZoneId.systemDefault()).toInstant());
		
		if(calendars==null || calendars.isEmpty()){return new ArrayList<ITEM>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ITEM> cQ = cB.createQuery(fbCalendar.getClassItem());
		Root<ITEM> root = cQ.from(fbCalendar.getClassItem());
		
		Expression<LocalDateTime> dStart = root.get(JeeslCalendarItem.Attributes.utcStart.toString());
		Expression<LocalDateTime> dEnd   = root.get(JeeslCalendarItem.Attributes.utcEnd.toString());
		
		//After
	    Predicate startAfterFrom = cB.greaterThanOrEqualTo(dStart, startLocalDate);
	    Predicate endAfterFrom = cB.greaterThanOrEqualTo(dEnd, startLocalDate);
	    Predicate endAfterTo = cB.greaterThanOrEqualTo(dEnd, endLocalDate);
	    
	    //Before
	    Predicate startBeforeTo = cB.lessThan(dStart, endLocalDate);
	    Predicate startBeforeFrom = cB.lessThan(dStart, startLocalDate);
	    Predicate endBeforeTo = cB.lessThan(dEnd, endLocalDate);
		
		Predicate pOnlyStartAndStartInRange = cB.and(cB.isNull(dEnd),startAfterFrom,startBeforeTo);
		Predicate pStartAndEndInRange = cB.and(cB.isNotNull(dEnd),startAfterFrom,endBeforeTo);
		Predicate pStartOutsideEndInRange = cB.and(cB.isNotNull(dEnd),startBeforeFrom,endAfterFrom,endBeforeTo);
		Predicate pStartInRangeEndOutside = cB.and(cB.isNotNull(dEnd),startAfterFrom,startBeforeTo,endAfterTo);
		Predicate pStartBeforeRangeEndAfterRange = cB.and(cB.isNotNull(dEnd),startBeforeFrom,endAfterTo);
		predicates.add(cB.or(pOnlyStartAndStartInRange,pStartAndEndInRange,pStartOutsideEndInRange,pStartInRangeEndOutside,pStartBeforeRangeEndAfterRange));
		
		Join<ITEM,CALENDAR> jCalendar = root.join(JeeslCalendarItem.Attributes.calendar.toString());
		predicates.add(cB.isTrue(jCalendar.in(calendars)));
				    
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);
		cQ.orderBy(cB.asc(dStart));
		
		return em.createQuery(cQ).getResultList();
	}
}