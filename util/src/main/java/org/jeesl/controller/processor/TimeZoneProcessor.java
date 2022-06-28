package org.jeesl.controller.processor;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

public class TimeZoneProcessor
{
	private final String tzFrom;
	private final String tzTo;
	
	public TimeZoneProcessor(String tzFrom, String tzTo)
	{
		this.tzFrom=tzFrom;
		this.tzTo=tzTo;
	}
	
	public Date project(Date d)
	{
		return project(d,tzFrom,tzTo);
	}
	
	public static Date project(Date d, String tzFrom, String tzTo)
	{
//		logger.info("Date "+d.toString());
		LocalDateTime ldt = LocalDateTime.fromDateFields(d);
		DateTime dtSrc = ldt.toDateTime().withZoneRetainFields(DateTimeZone.forID(tzFrom));	
		DateTime dtDst = dtSrc.withZone(DateTimeZone.forID(tzTo));
		
//		logger.info("ldt "+dtSrc.toString()+" "+dtSrc.toDate().getTime());
//		logger.info("DT Src "+dtSrc.toString()+" "+dtSrc.toDate().getTime());
//		logger.info("DT Dst "+dtDst.toString()+" "+dtDst.toDate().getTime());
		
		return dtDst.toLocalDateTime().toDate();
	}
	
	public static Date build(Date d, String tz)
	{
//		logger.info("Date "+d.toString());
		LocalDateTime ldt = LocalDateTime.fromDateFields(d);
		DateTime dtSrc = ldt.toDateTime().withZoneRetainFields(DateTimeZone.forID(tz));	
		
//		logger.info("ldt "+dtSrc.toString()+" "+dtSrc.toDate().getTime());
//		logger.info("DT Src "+dtSrc.toString()+" "+dtSrc.toDate().getTime());
//		logger.info("DT Dst "+dtDst.toString()+" "+dtDst.toDate().getTime());
		
		return dtSrc.toLocalDateTime().toDate();
	}
	
	
}