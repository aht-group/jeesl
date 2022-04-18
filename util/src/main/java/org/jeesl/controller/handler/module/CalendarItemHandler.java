package org.jeesl.controller.handler.module;

import java.io.Serializable;

import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.CalendarFactoryBuilder;
import org.jeesl.factory.ejb.module.calendar.EjbTimeZoneFactory;
import org.jeesl.factory.txt.module.calendar.TxtCalendarItemFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalendarItemHandler <L extends JeeslLang, D extends JeeslDescription,
									CALENDAR extends JeeslCalendar<L,D,CALENDAR,ZONE,CT,ITEM,IT>,
									ZONE extends JeeslCalendarTimeZone<L,D>,
									CT extends JeeslCalendarType<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<L,D,CALENDAR,ZONE,CT,ITEM,IT>,
									IT extends JeeslStatus<L,D,IT>
									>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(CalendarItemHandler.class);
	private boolean debug = true;

	protected final JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT> fCalendar;
	private final CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar;
	
	private final EjbTimeZoneFactory<L,D,CALENDAR,ZONE,CT,ITEM,IT> efZone;
	private final TxtCalendarItemFactory<L,D,CALENDAR,ZONE,CT,ITEM,IT> tfItem;
	
	private ZONE timeZone; public ZONE getTimeZone() {return timeZone;} public void setTimeZone(ZONE timeZone) {this.timeZone = timeZone;}

	private ITEM item; public ITEM getItem() {return item;} public void setItem(ITEM item) {this.item = item;}

	
	public CalendarItemHandler(final JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT> fCalendar,
			CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar)
	{
		this.fCalendar=fCalendar;
		this.fbCalendar=fbCalendar;
		
		efZone=fbCalendar.ejbZone();
		tfItem=fbCalendar.txtItem();
	}
	
	public void updateItem(ITEM item)
	{
		this.item = efZone.utc2Zones(item);
	}
	
	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		StringBuilder sb = new StringBuilder();

		item.setStartZone(fCalendar.find(fbCalendar.getClassZone(),item.getStartZone()));
		item.setEndZone(fCalendar.find(fbCalendar.getClassZone(),item.getEndZone()));
		
		if(debug)
		{
			sb.append("Saving ").append(item.getClass().getSimpleName());
			sb.append(" ").append(item.getStartZone().getCode());
			sb.append(" ").append(tfItem.debug(item));
		}
		
		item = efZone.toUtc(item);
		item.setType(fCalendar.find(fbCalendar.getClassItemType(),item.getType()));
		item = fCalendar.save(item);
		
		if(debug)
		{
			sb.append(" Converted ").append(tfItem.debug(item));
			logger.info(sb.toString());
		}
	}
	
	 public void toggleAllDay()
	 {
		 logger.info("toggleAllDay "+item.isAllDay());	 
	 }
}