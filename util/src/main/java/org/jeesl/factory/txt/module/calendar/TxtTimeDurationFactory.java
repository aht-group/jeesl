package org.jeesl.factory.txt.module.calendar;

import java.time.Duration;

import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtTimeDurationFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtTimeDurationFactory.class);
	
	public static enum UNITS{hourMinute,minuteSecondMilli}
	
	private String localeCode; 
	
	public static TxtTimeDurationFactory instance() {return new TxtTimeDurationFactory("en");}
	public TxtTimeDurationFactory(String localeCode)
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
	
	public String debugMillis(long millies)
	{
		
		return this.getClass().getSimpleName()+":NYI";
	}
	
	public String build(Duration d)
	{
		
		 long nanoseconds = d.toNanos();

	        // Berechne Stunden, Minuten, Sekunden und Nanosekunden
	        long hours = nanoseconds / 3_600_000_000_000L;
	        long minutes = (nanoseconds % 3_600_000_000_000L) / 60_000_000_000L;
	        long seconds = (nanoseconds % 60_000_000_000L) / 1_000_000_000L;
	        long remainingNanoseconds = nanoseconds % 1_000_000_000L;
		
		StringBuilder sb = new StringBuilder();
		
		
		if(hours>0)
		{
			sb.append(hours);
			sb.append(" ");
			if(hours==1) {sb.append("hour");} else {sb.append("hours");}
			sb.append(", ");
		}
		if(minutes>0)
		{
			sb.append(minutes);
			sb.append(" ");
			if(minutes==1) {sb.append("minute");} else {sb.append("minutes");}
			sb.append(", ");
		}
		if(seconds>0)
		{
			sb.append(seconds);
			sb.append(" ");
			if(seconds==1) {sb.append("second");} else {sb.append("seconds");}
			sb.append(", ");
		}
	
		sb.append(" ");
		sb.append(remainingNanoseconds);
		
		return sb.toString();
	}
}