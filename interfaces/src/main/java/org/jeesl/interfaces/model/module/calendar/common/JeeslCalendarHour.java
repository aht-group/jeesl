package org.jeesl.interfaces.model.module.calendar.common;

import java.io.Serializable;
import java.time.LocalTime;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarHourOfDay;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCalendarHour <DAY extends JeeslCalendarDay<?,?,?>,
									HOUR extends JeeslCalendarHourOfDay<?,?,HOUR,?>
									
								>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable
{
	public enum Att{id,day,hour}
		
	DAY getDay();
	void setDay(DAY day);
	
	HOUR getHour();
	void setHour(HOUR hour);
	
	LocalTime getTime();
	void setTime(LocalTime time);
}