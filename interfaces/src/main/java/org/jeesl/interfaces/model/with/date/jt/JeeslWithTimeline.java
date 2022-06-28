package org.jeesl.interfaces.model.with.date.jt;

import org.jeesl.interfaces.model.with.date.ju.EjbWithDateRange;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithTimeline extends EjbWithId,EjbWithDateRange
{	
	public boolean isAllDay();
	public void setAllDay(boolean allDay);
}