package org.jeesl.interfaces.model.module.calendar;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslCalendar <
								ZONE extends JeeslCalendarTimeZone<?,?>,
								CT extends JeeslCalendarType<?,?,CT,?>
								>
		extends Serializable,EjbWithId
{
//	void test();
}