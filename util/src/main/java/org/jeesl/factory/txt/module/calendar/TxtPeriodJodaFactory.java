package org.jeesl.factory.txt.module.calendar;

import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtPeriodJodaFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtPeriodJodaFactory.class);
	
	public static enum UNITS{hourMinute,minuteSecondMilli}
	
	private PeriodFormatter periodFormatter;
	
	PeriodType pt;
	
	public TxtPeriodJodaFactory()
	{
		periodFormatter = new PeriodFormatterBuilder()
				.appendWeeks().appendSuffix(" week", " weeks").appendSeparator(" ")
			    .appendDays().appendSuffix(" day", " days").appendSeparator(" ")
			    .appendHours().appendSuffix("hr", "hrs").appendSeparator(" ")
			    .appendMinutes().appendSuffix("min", "mins").appendSeparator(" ")
			    .appendSeconds().appendSuffix(" second", " secs").appendSeparator(" ")
				.appendMillis().appendSuffix(" ms", " ms")
				.toFormatter();
				
				//appendSuffix(" second", " secs").toFormatter();
		setUnits(UNITS.hourMinute);
	}
	
	public void setUnits(UNITS units)
	{
		DurationFieldType[] types=null;
		switch(units)
		{
			case hourMinute: types = new DurationFieldType[]{DurationFieldType.hours(),DurationFieldType.minutes()};break;
			case minuteSecondMilli: types = new DurationFieldType[]{DurationFieldType.minutes(),DurationFieldType.seconds(),DurationFieldType.millis()};break;
		}
		pt = PeriodType.forFields(types);
	}
	
	public String debug(int minutes)
	{
		Period p = Period.minutes(minutes);
		return periodFormatter.print(p.normalizedStandard(pt));
	}
	
	public String debugMillis(long millies)
	{
		Period p = Period.millis(Long.valueOf(millies).intValue());
		return periodFormatter.print(p.normalizedStandard(pt));
	}
}