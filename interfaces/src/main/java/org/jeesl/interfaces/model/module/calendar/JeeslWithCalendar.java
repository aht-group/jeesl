package org.jeesl.interfaces.model.module.calendar;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithCalendar <CAL extends JeeslCalendar<?,?>> extends EjbWithId
{
	public enum Attributes{calendar}
	
	CAL getCalendar();
	void setCalendar(CAL calendar);
}