package org.jeesl.api.facade.module;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslWithCalendar;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

public interface JeeslCalendarFacade <L extends JeeslLang, D extends JeeslDescription,
										CALENDAR extends JeeslCalendar<ZONE,CT>,
										ZONE extends JeeslCalendarTimeZone<L,D>,
										CT extends JeeslCalendarScope<L,D,CT,?>,
										ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT>,
										IT extends JeeslCalendarItemType<L,D,?,IT,?>
										>
			extends JeeslFacade
{
	<OWNER extends JeeslWithCalendar<CALENDAR>> CALENDAR fCalendar(Class<OWNER> cOwner, OWNER owner) throws JeeslNotFoundException;
	<OWNER extends JeeslWithCalendar<CALENDAR>> OWNER fCalendarOwner(Class<OWNER> cOwner, CALENDAR calendar) throws JeeslNotFoundException;
	
	<OWNER extends JeeslWithCalendar<CALENDAR>> Map<OWNER,CALENDAR> fCalendarOwners(Class<OWNER> cOwner, List<OWNER> owners);
	
	List<ITEM> fCalendarItems(ZONE zone, CALENDAR calendar, LocalDate from, LocalDate to);
	List<ITEM> fCalendarItems(ZONE zone, List<CALENDAR> calendars, LocalDate from, LocalDate to);
}