package org.jeesl.interfaces.model.module.calendar;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithTimeline;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslCalendarItem <CALENDAR extends JeeslCalendar<ZONE,?>,
									ZONE extends JeeslCalendarTimeZone<?,?>,
									IT extends JeeslCalendarItemType<?,?,?,IT,?>
									>
		extends  Serializable,EjbWithId,
					EjbSaveable,EjbRemoveable,EjbWithParentAttributeResolver,
					JeeslWithTimeline
{
	public enum Attributes {calendar,startDate,endDate}
	
	IT getType2();
	void setType2(IT type);

	ZONE getStartZone();
	void setStartZone(ZONE startZone);
	
	ZONE getEndZone();
	void setEndZone(ZONE endZone);
}