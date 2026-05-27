package org.jeesl.interfaces.model.module.calendar.common;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarDayOfMonth;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarMonth;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDate;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCalendarDay <YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
									MONTH extends JeeslCalendarMonth<?,?,MONTH,?>,
									DAY extends JeeslCalendarDayOfMonth<?,?,DAY,?>
								>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,JeeslWithRecordDate
{
	public enum Att{id,year,month,day,record}
//	void test();
	
	YEAR getYear();
	void setYear(YEAR year);
	
	MONTH getMonth();
	void setMonth(MONTH month);
	
	DAY getDay();
	void setDay(DAY day);
	
}