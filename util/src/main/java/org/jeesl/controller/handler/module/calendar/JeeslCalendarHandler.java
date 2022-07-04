package org.jeesl.controller.handler.module.calendar;

import java.io.Serializable;
import java.time.LocalDate;

import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.CalendarFactoryBuilder;
import org.jeesl.factory.txt.module.calendar.TxtCalendarItemFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCalendarHandler <L extends JeeslLang, D extends JeeslDescription,
									CALENDAR extends JeeslCalendar<ZONE,CT>,
									ZONE extends JeeslCalendarTimeZone<L,D>,
									CT extends JeeslCalendarType<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT>,
									IT extends JeeslCalendarItemType<L,D,?,IT,?>
									>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslCalendarHandler.class);
	protected boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	protected final JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT> fCalendar;
	private final CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar;
	
	private final TxtCalendarItemFactory<L,D,CALENDAR,ZONE,CT,ITEM,IT> tfItem;
	
	private ZONE timeZone; public ZONE getTimeZone() {return timeZone;} public void setTimeZone(ZONE timeZone) {this.timeZone = timeZone;}

	private ITEM item; public ITEM getItem() {return item;} public void setItem(ITEM item) {this.item = item;}

	protected LocalDate ldStart; public LocalDate getLdStart() {return ldStart;} public void setLdStart(LocalDate ldStart) {this.ldStart = ldStart;}
	protected LocalDate ldEnd; public LocalDate getLdEnd() {return ldEnd;} public void setLdEnd(LocalDate ldEnd) {this.ldEnd = ldEnd;}

	public JeeslCalendarHandler(final JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT> fCalendar,
			CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar)
	{
		this.fCalendar=fCalendar;
		this.fbCalendar=fbCalendar;
		
		debugOnInfo = false;
		
		tfItem=fbCalendar.txtItem();
	}
	
	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info("saveItem "+item.toString());}

		item.setStartZone(fCalendar.find(fbCalendar.getClassZone(),item.getStartZone()));
		item.setEndZone(fCalendar.find(fbCalendar.getClassZone(),item.getEndZone()));
		item.setType(fCalendar.find(fbCalendar.getClassItemType(),item.getType()));
		
		if(item.isAllDay())
		{
			item.setLocalStart(ldStart.atStartOfDay());
			item.setLocalEnd(ldStart.atStartOfDay());
		}
		
		item = fCalendar.save(item);
		
		if(debugOnInfo) {logger.info("saveItem completed "+tfItem.debug(item));}
	}
	
	 public void toggleAllDay()
	 {
		 logger.info("toggleAllDay "+item.isAllDay());	 
	 }
}