package org.jeesl.factory.xml.module.calendar;

import java.util.Date;

import org.jeesl.controller.processor.TimeZoneProcessor;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.module.calendar.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

public class XmlCalendarItemFactory <L extends JeeslLang, D extends JeeslDescription,
									CALENDAR extends JeeslCalendar<L,D,CALENDAR,ZONE,CT,ITEM,IT>,
									ZONE extends JeeslCalendarTimeZone<L,D,CALENDAR,ZONE,CT,ITEM,IT>,
									CT extends JeeslStatus<CT,L,D>,
									ITEM extends JeeslCalendarItem<L,D,CALENDAR,ZONE,CT,ITEM,IT>,
									IT extends JeeslStatus<IT,L,D>
									>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCalendarItemFactory.class);
	
	private final TimeZoneProcessor tzp;
	@SuppressWarnings("unused")
	private XmlTypeFactory<L,D,IT> xfType;
	
	public XmlCalendarItemFactory(String localeCode, Item q)
	{
		this(localeCode,q,null);
	}
	public XmlCalendarItemFactory(String localeCode, Item q, TimeZoneProcessor tzp)
	{
		this.tzp=tzp;
		if(q.isSetType()){xfType = new XmlTypeFactory<>(localeCode,q.getType());}
	}
	
	public Item build(ITEM item)
	{
		Item xml = build();		
//		if(q.isSetType()){xml.setType(xfType.build(calendar.g));
		xml.setType(XmlTypeFactory.create(item.getType().getCode()));
		
		if(tzp==null){xml.setStart(DateUtil.getXmlGc4D(item.getStartDate()));}
		else{xml.setStart(DateUtil.getXmlGc4D(tzp.project(item.getStartDate())));}
		
		if(tzp==null){xml.setEnd(DateUtil.getXmlGc4D(item.getEndDate()));}
		else{xml.setEnd(DateUtil.getXmlGc4D(tzp.project(item.getEndDate())));}
		
		xml.setAllDay(item.isAllDay());
		
		return xml;
	}
	
	public static Item build()
	{
		Item xml = new Item();		
		return xml;
	}
	
	public static Item build(Date date)
	{
		Item xml = build();		
		xml.setStart(DateUtil.getXmlGc4D(date));
		xml.setEnd(DateUtil.getXmlGc4D(date));
		xml.setAllDay(true);
		return xml;
	}
}