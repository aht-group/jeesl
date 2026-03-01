package org.jeesl.interfaces.bean.module;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;

public interface JeeslAppCalendarBean<L extends JeeslLang, D extends JeeslDescription,
										CALENDAR extends JeeslCalendar<ZONE,CT>,
										ZONE extends JeeslCalendarZone<L,D>,
										CT extends JeeslCalendarScope<L,D,CT,?>,
										ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
										IT extends JeeslCalendarItemType<L,D,?,IT,?>,
										USER extends JeeslSimpleUser>
								extends Serializable
{
	List<ZONE> getZones();
	ZONE getZoneDefault();
	void reloadZones(JeeslFacade facade);
}