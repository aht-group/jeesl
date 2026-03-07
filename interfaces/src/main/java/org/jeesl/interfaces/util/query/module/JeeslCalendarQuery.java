package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarDayOfMonth;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarMonth;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslCalendarQuery<YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
								MONTH extends JeeslCalendarMonth<?,?,MONTH,?>,
								DOM extends JeeslCalendarDayOfMonth<?,?,DOM,?>,
								CAL extends JeeslCalendar<?,?>
								
>
			extends JeeslCoreQuery
{
//	void x();
	
	List<YEAR> getCalYears();
	List<DOM> getCalDaysOfMonth();
	
	List<CAL> getCalendars();
//	List<TYPE> getTypes();
}