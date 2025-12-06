package org.jeesl.controller.facade.jx.module;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.CalendarFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslWithCalendar;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslCalendarQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCalendarFacadeBean<L extends JeeslLang, D extends JeeslDescription,
									YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
									CALENDAR extends JeeslCalendar<ZONE,CT>,
									ZONE extends JeeslCalendarTimeZone<L,D>,
									CT extends JeeslCalendarScope<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
									IT extends JeeslCalendarItemType<L,D,?,IT,?>,
									USER extends JeeslSimpleUser>
					extends JeeslFacadeBean
					implements JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslCalendarFacadeBean.class);

	private final CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fbCalendar;
	
	public JeeslCalendarFacadeBean(EntityManager em, CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fbCalendar)
	{
		super(em);
		this.fbCalendar=fbCalendar;
	}
	
	@Override public <OWNER extends JeeslWithCalendar<CALENDAR>> CALENDAR fCalendar(Class<OWNER> cOwner, OWNER owner) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CALENDAR> cQ = cB.createQuery(fbCalendar.getClassCalendar());
		Root<OWNER> root = cQ.from(cOwner);
		
		Path<CALENDAR> pathCalendar = root.get(JeeslWithCalendar.Attributes.calendar.toString());
		Path<Long> pId = root.get(EjbWithId.attribute);
		
		cQ.select(pathCalendar);
		cQ.where(cB.equal(pId,owner.getId()));
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbCalendar.getClassCalendar()+" found for owner "+owner);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple "+fbCalendar.getClassCalendar()+" found for owner "+owner);}
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
	
	@Override public <OWNER extends JeeslWithCalendar<CALENDAR>> Map<OWNER,CALENDAR> fCalendarOwners(Class<OWNER> cOwner, List<OWNER> owners)
	{
		Map<OWNER,CALENDAR> map = new HashMap<>();
		if(owners==null || owners.isEmpty()) {return map;}
		
		List<OWNER> list = this.find(cOwner,EjbIdFactory.toIds(owners));
		for(OWNER o : list)
		{
			if(o.getCalendar()!=null)
			{
				CALENDAR calendar = o.getCalendar();
				calendar.getId();
				map.put(o,calendar);
			}
		}
//		CriteriaBuilder cB = em.getCriteriaBuilder();
//		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
//		 
//		Root<AhtUser> user = cQ.from(AhtUser.class);
//		predicates.add(user.in(users));
//		
//		Join<AhtUser,ErpUserBox> jBox = user.join(AhtUser.Attributes.box.toString());
//		Join<ErpUserBox,CalCalendar> jCalendar = jBox.join(ErpUserBox.Attributes.calendar.toString());
//
//		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
//		cQ.multiselect(user.get("id"),jCalendar.get("id"));
//		
//		TypedQuery<Tuple> tQ = em.createQuery(cQ);
//        List<Tuple> tuples = tQ.getResultList();
//        
//        for(Tuple t : tuples)
//        {
//        	long userId = (Long)t.get(0);
//        	long calendarId = (Long)t.get(1);
//        	try
//        	{
//				map.put(this.find(AhtUser.class,userId),this.find(CalCalendar.class, calendarId));
//			}
//        	catch (JeeslNotFoundException e) {e.printStackTrace();}
//        }
		
		return map;
	}
	
	@Override public List<ITEM> fCalendarItems(JeeslCalendarQuery<YEAR,CALENDAR> query)
	{
		if(ObjectUtils.isEmpty(query.getCalendars())){return new ArrayList<>();}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ITEM> cQ = cB.createQuery(fbCalendar.getClassItem());
		Root<ITEM> root = cQ.from(fbCalendar.getClassItem());
		
		Expression<LocalDateTime> utcStart = root.get(JeeslCalendarItem.Attributes.utcStart.toString());
		
		if(ObjectUtils.isNotEmpty(query.getCalendars()))
		{
			Join<ITEM,CALENDAR> jCalendar = root.join(JeeslCalendarItem.Attributes.calendar.toString());
			predicates.add(jCalendar.in(query.getCalendars()));
		}
		
		cQ.select(root);	    
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.orderBy(cB.asc(utcStart));
		
		return em.createQuery(cQ).getResultList();
	}

	@Override public List<ITEM> fCalendarItems(ZONE zone, CALENDAR calendar, LocalDate from, LocalDate to)
	{
		List<CALENDAR> calendars = new ArrayList<CALENDAR>();
		calendars.add(calendar);
		return fCalendarItems(zone,calendars,from,to);
	}
	
	@Override public List<ITEM> fCalendarItems(ZONE zone, List<CALENDAR> calendars, LocalDate ldStart, LocalDate ldEnd)
	{	
		if(ObjectUtils.isEmpty(calendars)){return new ArrayList<>();}
		
		LocalDateTime ldtStart = ldStart.atStartOfDay(ZoneId.of(zone.getCode())).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
		LocalDateTime ldtEnd = ldEnd.atStartOfDay(ZoneId.of(zone.getCode())).plusDays(1).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<ITEM> cQ = cB.createQuery(fbCalendar.getClassItem());
		Root<ITEM> root = cQ.from(fbCalendar.getClassItem());
		
		Expression<LocalDateTime> dStart = root.get(JeeslCalendarItem.Attributes.utcStart.toString());
		Expression<LocalDateTime> dEnd   = root.get(JeeslCalendarItem.Attributes.utcEnd.toString());
		
		//After
	    Predicate startAfterFrom = cB.greaterThanOrEqualTo(dStart, ldtStart);
	    Predicate endAfterFrom = cB.greaterThanOrEqualTo(dEnd, ldtStart);
	    Predicate endAfterTo = cB.greaterThanOrEqualTo(dEnd, ldtEnd);
	    
	    //Before
	    Predicate startBeforeTo = cB.lessThan(dStart, ldtEnd);
	    Predicate startBeforeFrom = cB.lessThan(dStart, ldtStart);
	    Predicate endBeforeTo = cB.lessThan(dEnd, ldtEnd);
		
		Predicate pOnlyStartAndStartInRange = cB.and(cB.isNull(dEnd),startAfterFrom,startBeforeTo);
		Predicate pStartAndEndInRange = cB.and(cB.isNotNull(dEnd),startAfterFrom,endBeforeTo);
		Predicate pStartOutsideEndInRange = cB.and(cB.isNotNull(dEnd),startBeforeFrom,endAfterFrom,endBeforeTo);
		Predicate pStartInRangeEndOutside = cB.and(cB.isNotNull(dEnd),startAfterFrom,startBeforeTo,endAfterTo);
		Predicate pStartBeforeRangeEndAfterRange = cB.and(cB.isNotNull(dEnd),startBeforeFrom,endAfterTo);
		predicates.add(cB.or(pOnlyStartAndStartInRange,pStartAndEndInRange,pStartOutsideEndInRange,pStartInRangeEndOutside,pStartBeforeRangeEndAfterRange));
		
		Join<ITEM,CALENDAR> jCalendar = root.join(JeeslCalendarItem.Attributes.calendar.toString());
		predicates.add(jCalendar.in(calendars));
				 
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		cQ.orderBy(cB.asc(dStart));
		
		return em.createQuery(cQ).getResultList();
	}
	
	public static Predicate dateRangeInRange(CriteriaBuilder cB, Expression<LocalDate> dStart, Expression<LocalDate> dEnd, LocalDate from, LocalDate to)
	{
		//After
	    Predicate startAfterFrom = cB.greaterThanOrEqualTo(dStart, from);
	    Predicate endAfterFrom = cB.greaterThanOrEqualTo(dEnd, from);
	    Predicate endAfterTo = cB.greaterThanOrEqualTo(dEnd, to);

	    //Before
	    Predicate startBeforeTo = cB.lessThanOrEqualTo(dStart, to);
	    Predicate startBeforeFrom = cB.lessThanOrEqualTo(dStart, from);
	    Predicate endBeforeTo = cB.lessThanOrEqualTo(dEnd, to);

		Predicate pOnlyStartAndStartInRange = cB.and(cB.isNull(dEnd),startAfterFrom,startBeforeTo);
		Predicate pStartAndEndInRange = cB.and(cB.isNotNull(dEnd),startAfterFrom,endBeforeTo);
		Predicate pStartOutsideEndInRange = cB.and(cB.isNotNull(dEnd),startBeforeFrom,endAfterFrom,endBeforeTo);
		Predicate pStartInRangeEndOutside = cB.and(cB.isNotNull(dEnd),startAfterFrom,startBeforeTo,endAfterTo);
		Predicate pStartBeforeRangeEndAfterRange = cB.and(cB.isNotNull(dEnd),startBeforeFrom,endAfterTo);

		return cB.or(pOnlyStartAndStartInRange,pStartAndEndInRange,pStartOutsideEndInRange,pStartInRangeEndOutside,pStartBeforeRangeEndAfterRange);
	}
}