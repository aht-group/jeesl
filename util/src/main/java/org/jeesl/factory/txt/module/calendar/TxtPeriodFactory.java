package org.jeesl.factory.txt.module.calendar;

import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtPeriodFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtPeriodFactory.class);
	
	public static enum UNITS{hourMinute,minuteSecondMilli}
	
	private PeriodFormatter periodFormatter;
	
	PeriodType pt;
	
	public TxtPeriodFactory()
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
	
	
	// The following is without JODA!
	private String localeCode; 
	
	public TxtPeriodFactory(String localeCode)
	{  
		this.localeCode=localeCode;
	}
	    
	public String build(java.time.Period period)
	{
		StringBuilder sb = new StringBuilder();
		
		int years = period.getYears();
		int months = period.getMonths();
		int days = period.getDays();
		
		if(years>0) {year(sb,years);}
		if(months>0)
		{
			if(sb.length()>0) {sb.append(", ");}
			month(sb,months);
		}
		if(days>0)
		{
			if(sb.length()>0) {sb.append(", ");}
			day(sb,days);
		}
		
		return sb.toString();
	}

	private void year(StringBuilder sb, int years)
	{
		sb.append(years);
		sb.append(" ");
		if(years==1)
		{
			if(localeCode.equals("de")) {sb.append("Jahr");}
		}
		else
		{
			if(localeCode.equals("de")) {sb.append("Jahre");}
		}
	}
	
	private void month(StringBuilder sb, int value)
	{
		sb.append(value);
		sb.append(" ");
		if(value==1)
		{
			if(localeCode.equals("de")) {sb.append("Monat");}
		}
		else
		{
			if(localeCode.equals("de")) {sb.append("Monate");}
		}
	}
	
	private void day(StringBuilder sb, int value)
	{
		sb.append(value);
		sb.append(" ");
		if(value==1)
		{
			if(localeCode.equals("de")) {sb.append("Tag");}
		}
		else
		{
			if(localeCode.equals("de")) {sb.append("Tage");}
		}
	}
}