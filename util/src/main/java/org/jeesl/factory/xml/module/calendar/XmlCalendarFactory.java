package org.jeesl.factory.xml.module.calendar;

import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.module.calendar.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCalendarFactory <L extends JeeslLang, D extends JeeslDescription,
									CALENDAR extends JeeslCalendar<L,D,CALENDAR,ZONE,CT,ITEM,IT>,
									ZONE extends JeeslCalendarTimeZone<L,D>,
									CT extends JeeslCalendarType<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<L,D,CALENDAR,ZONE,CT,ITEM,IT>,
									IT extends JeeslStatus<L,D,IT>
									>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCalendarFactory.class);
	
	@SuppressWarnings("unused")
	private XmlTypeFactory<L,D,CT> xfType;
	
	
	
	public XmlCalendarFactory(String localeCode, Calendar q)
	{
		if(q.isSetType()){xfType = new XmlTypeFactory<>(localeCode,q.getType());}
	}
	
	public Calendar build(CALENDAR calendar)
	{
		Calendar xml = build();		
//		if(q.isSetType()){xml.setType(xfType.build(calendar.g));
		
		return xml;
	}
	
	public static Calendar build()
	{
		Calendar xml = new Calendar();		
		return xml;
	}
}