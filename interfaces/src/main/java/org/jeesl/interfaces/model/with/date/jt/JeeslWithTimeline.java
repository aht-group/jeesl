package org.jeesl.interfaces.model.with.date.jt;

import org.jeesl.interfaces.model.with.date.ju.EjbWithDateRange;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithTimeline extends EjbWithId,
						EjbWithDateRange	// This is Java7 implementation with java.util.Date
//						JeeslWithDateTimeRange	// This is Java8 implementation with 
{	
	public boolean isAllDay();
	public void setAllDay(boolean allDay);
}