package org.jeesl.factory.xml.module.calendar;

import java.util.Objects;

import org.jeesl.factory.xml.io.locale.status.XmlTypeFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.calendar.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCalendarFactory <L extends JeeslLang, D extends JeeslDescription,
									CALENDAR extends JeeslCalendar<ZONE,CT>,
									ZONE extends JeeslCalendarZone<L,D>,
									CT extends JeeslCalendarScope<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,?>,
									IT extends JeeslCalendarItemType<L,D,?,IT,?>
									>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCalendarFactory.class);
	
	@SuppressWarnings("unused")
	private XmlTypeFactory<L,D,CT> xfType;
	
	public XmlCalendarFactory(String localeCode, Calendar q)
	{
		if(Objects.nonNull(q.getType())) {xfType = new XmlTypeFactory<>(localeCode,q.getType());}
	}
	
	public Calendar build(CALENDAR calendar)
	{
		Calendar xml = build();		
//		if(Objects.nonNull(q.getType())) {xml.setType(xfType.build(calendar.g));
		
		return xml;
	}
	
	public static Calendar build()
	{
		Calendar xml = new Calendar();		
		return xml;
	}
}