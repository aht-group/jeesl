package org.jeesl.factory.xml.module.calendar;

import java.util.Date;
import java.util.Objects;

import org.exlp.util.system.DateUtil;
import org.jeesl.controller.processor.TimeZoneProcessor;
import org.jeesl.factory.xml.io.locale.status.XmlTypeFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.model.xml.module.calendar.Item;
import org.jeesl.util.query.xml.module.XmlCalendarQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCalendarItemFactory <L extends JeeslLang, D extends JeeslDescription,
									CALENDAR extends JeeslCalendar<ZONE,CT>,
									ZONE extends JeeslCalendarZone<L,D>,
									CT extends JeeslCalendarScope<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
									IT extends JeeslCalendarItemType<L,D,?,IT,?>,
									USER extends JeeslSimpleUser>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCalendarItemFactory.class);
	
	private final Item q;
//	private final TimeZoneProcessor tzp;
	private XmlTypeFactory<L,D,IT> xfType;
	
	public XmlCalendarItemFactory(String localeCode, Item q, TimeZoneProcessor tzp)
	{
		this.q=q;
//		this.tzp=tzp;
		if(Objects.nonNull(q.getType())) {xfType = new XmlTypeFactory<>(localeCode,q.getType());}
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					CALENDAR extends JeeslCalendar<ZONE,CT>,
					ZONE extends JeeslCalendarZone<L,D>,
					CT extends JeeslCalendarScope<L,D,CT,?>,
					ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
					IT extends JeeslCalendarItemType<L,D,?,IT,?>,
					USER extends JeeslSimpleUser
					>
	XmlCalendarItemFactory<L,D,CALENDAR,ZONE,CT,ITEM,IT,USER> instance(String localeCode, XmlCalendarQuery.Key key)
	{
		return new XmlCalendarItemFactory<>(localeCode,XmlCalendarQuery.get(key, localeCode).getItem(),null);
	}
	
	public Item build(ITEM item)
	{
		Item xml = build();		
		if(Objects.nonNull(q.getType())) {xml.setType(xfType.build(item.getType()));}
		
		
		logger.error("NYI XML Date conversion");
		
//		if(tzp==null){xml.setStart(DateUtil.getXmlGc4D(item.getStartDate()));}
//		else{xml.setStart(DateUtil.getXmlGc4D(tzp.project(item.getStartDate())));}
//		
//		if(tzp==null){xml.setEnd(DateUtil.getXmlGc4D(item.getEndDate()));}
//		else{xml.setEnd(DateUtil.getXmlGc4D(tzp.project(item.getEndDate())));}
		
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
		xml.setStart(DateUtil.toXmlGc(date));
		xml.setEnd(DateUtil.toXmlGc(date));
		xml.setAllDay(true);
		return xml;
	}
}