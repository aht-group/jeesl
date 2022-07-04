package org.jeesl.interfaces.model.module.calendar;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithTimeline;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslCalendarItem <CALENDAR extends JeeslCalendar<ZONE,?>,
									ZONE extends JeeslCalendarTimeZone<?,?>,
									IT extends JeeslCalendarItemType<?,?,?,IT,?>
									>
		extends  Serializable,EjbWithId,
					EjbSaveable,EjbRemoveable,EjbWithParentAttributeResolver
//					,JeeslWithTimeline
{
	public enum Attributes {calendar,utcStart,utcEnd}
	
	IT getType();
	void setType(IT type);

	ZONE getStartZone();
	void setStartZone(ZONE startZone);
	
	ZONE getEndZone();
	void setEndZone(ZONE endZone);
	
	LocalDateTime getLocalStart();
	void setLocalStart(LocalDateTime localStart);
	
	LocalDateTime getLocalEnd();
	void setLocalEnd(LocalDateTime localEnd);
	
	LocalDateTime getUtcStart();
	public void setUtcStart(LocalDateTime utcStart);
	
	LocalDateTime getUtcEnd();
	void setUtcEnd(LocalDateTime utcEnd);
	
	public boolean isAllDay();
	public void setAllDay(boolean allDay);
}