package org.jeesl.factory.txt.module.calendar;

import java.util.Date;

import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtCalendarItemFactory<L extends JeeslLang, D extends JeeslDescription,
								CALENDAR extends JeeslCalendar<ZONE,CT>,
								ZONE extends JeeslCalendarTimeZone<L,D>,
								CT extends JeeslCalendarType<L,D,CT,?>,
								ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT>,
								IT extends JeeslStatus<L,D,IT>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtCalendarItemFactory.class);
    
	public TxtCalendarItemFactory()
	{  

	}
	    
	public String debug(ITEM item)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("[").append(item.getId()).append("]");
		sb.append(" ").append(debug(item.getStartDate(),item.getEndDate()));
		
		return sb.toString();
	}

	public static String debug(Date start, Date end)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(dateTime(start));
		sb.append(" -> ").append(dateTime(end));
		return sb.toString();
	}
	
	private static String dateTime(Date date)
	{
		StringBuffer sb = new StringBuffer();
		
		DateTime dt = new DateTime(date);
		sb.append(dt.getHourOfDay());
		
		return sb.toString();
	}
}