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
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.util.query.module.JeeslCalendarQuery;

public interface JeeslCalendarFacade <L extends JeeslLang, D extends JeeslDescription,
										YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
										CAL extends JeeslCalendar<ZONE,CT>,
										ZONE extends JeeslCalendarTimeZone<L,D>,
										CT extends JeeslCalendarScope<L,D,CT,?>,
										ITEM extends JeeslCalendarItem<CAL,ZONE,TYPE,USER>,
										TYPE extends JeeslCalendarItemType<L,D,?,TYPE,?>,
										USER extends JeeslSimpleUser
										>
			extends JeeslFacade
{
	<OWNER extends JeeslWithCalendar<CAL>> CAL fCalendar(Class<OWNER> cOwner, OWNER owner) throws JeeslNotFoundException;
	<OWNER extends JeeslWithCalendar<CAL>> OWNER fCalendarOwner(Class<OWNER> cOwner, CAL calendar) throws JeeslNotFoundException;
	
	<OWNER extends JeeslWithCalendar<CAL>> Map<OWNER,CAL> fCalendarOwners(Class<OWNER> cOwner, List<OWNER> owners);
	
	List<ITEM> fCalendarItems(JeeslCalendarQuery<YEAR,CAL> query);
	List<ITEM> fCalendarItems(ZONE zone, CAL calendar, LocalDate from, LocalDate to);
	List<ITEM> fCalendarItems(ZONE zone, List<CAL> calendars, LocalDate from, LocalDate to);
}