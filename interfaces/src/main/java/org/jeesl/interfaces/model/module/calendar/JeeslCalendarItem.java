package org.jeesl.interfaces.model.module.calendar;

import java.io.Serializable;
import java.util.Date;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslCalendarItem <L extends JeeslLang, D extends JeeslDescription,
									CALENDAR extends JeeslCalendar<L,D,CALENDAR,ZONE,CT,ITEM,IT>,
									ZONE extends JeeslCalendarTimeZone<L,D>,
									CT extends JeeslCalendarType<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<L,D,CALENDAR,ZONE,CT,ITEM,IT>,
									IT extends JeeslStatus<L,D,IT>
									>
		extends  Serializable,EjbWithId,
					EjbSaveable,EjbRemoveable,EjbWithParentAttributeResolver
{
	public enum Attributes {calendar,startDate,endDate}
	
	IT getType();
	void setType(IT type);
	
	Date getStartDate();
	void setStartDate(Date startDate);

	ZONE getStartZone();
	void setStartZone(ZONE startZone);
	
	ZONE getEndZone();
	void setEndZone(ZONE endZone);
	
	Date getEndDate();
	void setEndDate(Date endDate);
	
	boolean isAllDay();
	void setAllDay(boolean allDay);
}