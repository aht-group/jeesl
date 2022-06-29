package org.jeesl.api.facade.module;

import java.time.LocalDateTime;
import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslCalendarFacade <L extends JeeslLang, D extends JeeslDescription,
										CALENDAR extends JeeslCalendar<ZONE,CT>,
										ZONE extends JeeslCalendarTimeZone<L,D>,
										CT extends JeeslCalendarType<L,D,CT,?>,
										ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT>,
										IT extends JeeslStatus<L,D,IT>
										>
			extends JeeslFacade
{	
	List<ITEM> fCalendarItems(CALENDAR calendar, LocalDateTime from, LocalDateTime to);
	List<ITEM> fCalendarItems(List<CALENDAR> calendars, LocalDateTime from, LocalDateTime to);
}