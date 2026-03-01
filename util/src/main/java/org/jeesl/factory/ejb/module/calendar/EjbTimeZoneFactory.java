package org.jeesl.factory.ejb.module.calendar;

import java.util.TimeZone;

import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTimeZoneFactory<ZONE extends JeeslCalendarZone<?,?>,
								ITEM extends JeeslCalendarItem<?,ZONE,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTimeZoneFactory.class);
	
	private final Class<ZONE> cZone;
    
	public EjbTimeZoneFactory(final Class<ZONE> cZone)
	{  
        this.cZone = cZone;
	}
	    
	public ZONE build()
	{
		ZONE ejb = null;
		try
		{
			ejb = cZone.newInstance();
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
//	public ITEM toUtc(ITEM item)
//	{
//		item.setStartDate(startToUtc(item));
//		item.setEndDate(endToUtc(item));
//		return item;
//	}
//	public Date startToUtc(ITEM item){return toUtc(item.getStartDate(),item.getStartZone().getCode());}
//	public Date endToUtc(ITEM item){return toUtc(item.getEndDate(),item.getEndZone().getCode());}
//	public static Date toUtc(Date d, String tzCode){return TimeZoneProcessor.project(d,tzCode,JeeslCalendarTimeZone.tzUtc);}
//	
//	public ITEM fromUtc(ITEM item, ZONE zone)
//	{
//		item.setStartDate(TimeZoneProcessor.project(item.getStartDate(),JeeslCalendarTimeZone.tzUtc,zone.getCode()));
//		item.setEndDate(TimeZoneProcessor.project(item.getEndDate(),JeeslCalendarTimeZone.tzUtc,zone.getCode()));
//		return item;
//	}
//	
//	public ITEM utc2Zones(ITEM item)
//	{
//		item.setStartDate(TimeZoneProcessor.project(item.getStartDate(),JeeslCalendarTimeZone.tzUtc,item.getStartZone().getCode()));
//		item.setEndDate(TimeZoneProcessor.project(item.getEndDate(),JeeslCalendarTimeZone.tzUtc,item.getEndZone().getCode()));
//		return item;
//	}
//
//	public Date startFromUtc(ITEM item, ZONE zone){return TimeZoneProcessor.project(item.getStartDate(),JeeslCalendarTimeZone.tzUtc,zone.getCode());}
//	public Date endFromUtc(ITEM item, ZONE zone){return TimeZoneProcessor.project(item.getEndDate(),JeeslCalendarTimeZone.tzUtc,zone.getCode());}
//
//	public Date toDate(Date date, ZONE zone)
//	{
//		return TimeZoneProcessor.build(date, zone.getCode());
//	}
//	
	public static boolean supportedCode(String code)
	{
		for(String s : TimeZone.getAvailableIDs())
		{
			if(s.equals(code)){return true;}
		}
		return false;
	}
}