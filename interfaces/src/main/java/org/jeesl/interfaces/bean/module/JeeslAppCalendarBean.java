package org.jeesl.interfaces.bean.module;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

public interface JeeslAppCalendarBean<L extends JeeslLang, D extends JeeslDescription,
										CALENDAR extends JeeslCalendar<ZONE,CT>,
										ZONE extends JeeslCalendarTimeZone<L,D>,
										CT extends JeeslCalendarType<L,D,CT,?>,
										ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT>,
										IT extends JeeslCalendarItemType<L,D,?,IT,?>>
								extends Serializable
{
	List<ZONE> getZones();
	ZONE getZoneDefault();
	void reloadZones(JeeslFacade facade);
}