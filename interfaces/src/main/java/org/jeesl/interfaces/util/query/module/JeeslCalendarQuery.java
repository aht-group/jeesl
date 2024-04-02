package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslCalendarQuery<CAL extends JeeslCalendar<?,?>,
								TYPE extends JeeslCalendarItemType<?,?,?,TYPE,?>
>
			extends JeeslCoreQuery
{
	List<CAL> getCalendars();
	List<TYPE> getTypes();
}