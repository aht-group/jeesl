package org.jeesl.controller.handler.module.calendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.CalendarFactoryBuilder;
import org.jeesl.factory.ejb.io.label.revision.data.EjbLastModifiedFactory;
import org.jeesl.factory.ejb.module.calendar.EjbCalItemFactory;
import org.jeesl.factory.txt.module.calendar.TxtCalendarItemFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCalendarHandler <L extends JeeslLang, D extends JeeslDescription,
									YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
									CALENDAR extends JeeslCalendar<ZONE,CT>,
									ZONE extends JeeslCalendarZone<L,D>,
									CT extends JeeslCalendarScope<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
									IT extends JeeslCalendarItemType<L,D,?,IT,?>,
									USER extends JeeslSimpleUser>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslCalendarHandler.class);
	protected boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	protected final JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fCalendar;
	protected final CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fbCalendar;
	
	protected EjbCalItemFactory<CALENDAR,ZONE,ITEM,IT> efItem;
	
	private final TxtCalendarItemFactory<L,D,CALENDAR,ZONE,CT,ITEM,IT> tfItem;
	
	private ZONE timeZone; public ZONE getTimeZone() {return timeZone;} public void setTimeZone(ZONE timeZone) {this.timeZone = timeZone;}

	private ITEM item; public ITEM getItem() {return item;} public void setItem(ITEM item) {this.item = item;}
	private USER user; public USER getUser() {return user;} public void setUser(USER user) {this.user = user;}

	protected LocalDate ldStart; public LocalDate getLdStart() {return ldStart;} public void setLdStart(LocalDate ldStart) {this.ldStart = ldStart;}
	protected LocalDate ldEnd; public LocalDate getLdEnd() {return ldEnd;} public void setLdEnd(LocalDate ldEnd) {this.ldEnd = ldEnd;}

	public JeeslCalendarHandler(final JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fCalendar, CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fbCalendar)
	{
		this.fCalendar=fCalendar;
		this.fbCalendar=fbCalendar;
		
		debugOnInfo = false;
		
		tfItem=fbCalendar.txtItem();
		efItem = fbCalendar.ejbItem();
	}
	
	public void cancelItem()
	{
		item=null;
	}
	
	public void onDateSelect(SelectEvent<LocalDateTime> selectEvent)
	{
		 if(debugOnInfo) {logger.info("onDateSelect: ");}
		 LocalDateTime ldt = selectEvent.getObject();
		 ldStart = ldt.toLocalDate();
		 ldEnd = ldt.toLocalDate();
		 
		 this.setItem(efItem.build(null,null,"",ldt,ldt));
	}
	
	public void selectItem()
	{
		if(debugOnInfo) {logger.info("selectItem: "+item.toString());}
		if(item.isAllDay())
		{
			ldStart = item.getLocalStart().toLocalDate();
			ldEnd = item.getLocalEnd().toLocalDate();
		}
	}
	
	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info("saveItem "+item.toString());}

		efItem.converter(fCalendar,item);
		
		if(item.isAllDay())
		{
			item.setLocalStart(ldStart.atStartOfDay());
			item.setLocalEnd(ldEnd.atStartOfDay());
		}
		
		EjbLastModifiedFactory.modified(user,item);
		item = fCalendar.save(item);
		
		if(debugOnInfo) {logger.info("saveItem completed "+tfItem.debug(item));}
	}
	
	public void deleteItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info("deleteItem "+item.toString());}
		fCalendar.rm(item);
		item = null;
	}
	
	 public void toggleAllDay()
	 {
		 logger.info("toggleAllDay "+item.isAllDay());	 
	 }
}