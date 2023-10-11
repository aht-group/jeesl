package org.jeesl.factory.ejb.module.calendar;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarDayOfWeek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCalDayOfWeekFactory<DOW extends JeeslCalendarDayOfWeek<?,?,DOW,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbCalDayOfWeekFactory.class);
	
	@SuppressWarnings("unused")
	private final Class<DOW> cDow;
	
	public static <DOW extends JeeslCalendarDayOfWeek<?,?,DOW,?>> EjbCalDayOfWeekFactory<DOW> instance(Class<DOW> cDow) {return new EjbCalDayOfWeekFactory<>(cDow);}
	private EjbCalDayOfWeekFactory(Class<DOW> cDow)
	{
		this.cDow = cDow;
	}
	
	public void nowAndReverse(List<DOW> list)
	{
		LocalDate now = LocalDate.now();
//		logger.info("Now.dow: "+now.getDayOfWeek());
		Collections.rotate(list,1-now.getDayOfWeek().getValue());
		Collections.rotate(list,-1);
		Collections.reverse(list);
	}
}