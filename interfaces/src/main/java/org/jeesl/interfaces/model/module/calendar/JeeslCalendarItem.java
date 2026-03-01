package org.jeesl.interfaces.model.module.calendar;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jeesl.interfaces.model.io.label.revision.data.JeeslLastModified;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCalendarItem <CAL extends JeeslCalendar<ZONE,?>,
									ZONE extends JeeslCalendarZone<?,?>,
									TYPE extends JeeslCalendarItemType<?,?,?,TYPE,?>,
									USER extends JeeslSimpleUser>
		extends  Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
					EjbWithParentAttributeResolver,
					JeeslLastModified<USER>
{
	public enum Attributes {calendar,utcStart,utcEnd}
	
	CAL getCalendar();
	void setCalendar(CAL calendar);
	
	String getTitle();
	void setTitle(String title);
	
	String getPlace();
	void setPlace(String place);
	
	TYPE getType();
	void setType(TYPE type);

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