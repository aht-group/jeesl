package org.jeesl.interfaces.model.module.calendar;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;

public interface JeeslCalendar <
								ZONE extends JeeslCalendarTimeZone<?,?>,
								CT extends JeeslCalendarType<?,?,CT,?>
								>
		extends Serializable,EjbWithId,EjbSaveable,JeeslWithType<CT>
{
//	void test();
}