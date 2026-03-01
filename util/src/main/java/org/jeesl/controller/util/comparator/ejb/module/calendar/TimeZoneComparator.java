package org.jeesl.controller.util.comparator.ejb.module.calendar;

import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeZoneComparator<L extends JeeslLang, D extends JeeslDescription,
									CALENDAR extends JeeslCalendar<ZONE,CT>,
									ZONE extends JeeslCalendarZone<L,D>,
									CT extends JeeslCalendarScope<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,?>,
									IT extends JeeslCalendarItemType<L,D,?,IT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TimeZoneComparator.class);

    public enum Type {offset};
    
    public Comparator<ZONE> factory(Type type)
    {
        Comparator<ZONE> c = null;
        TimeZoneComparator<L,D,CALENDAR,ZONE,CT,ITEM,IT> factory = new TimeZoneComparator<L,D,CALENDAR,ZONE,CT,ITEM,IT>();
        switch (type)
        {
            case offset: c = factory.new OffsetComparator();break;
        }

        return c;
    }

    private class OffsetComparator implements Comparator<ZONE>
    {
        public int compare(ZONE a, ZONE b)
        {
        	Date d = new Date();
        	TimeZone tzA = TimeZone.getTimeZone(a.getCode());
        	TimeZone tzB = TimeZone.getTimeZone(b.getCode());
        	
        	CompareToBuilder ctb = new CompareToBuilder();
        	ctb.append(tzA.getOffset(d.getTime()), tzB.getOffset(d.getTime()));
        	ctb.append(a.getCode(),b.getCode());
        	return ctb.toComparison();
        }
    }
}