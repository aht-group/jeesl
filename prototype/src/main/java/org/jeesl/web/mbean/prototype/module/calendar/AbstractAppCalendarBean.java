package org.jeesl.web.mbean.prototype.module.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.controller.util.comparator.ejb.module.calendar.TimeZoneComparator;
import org.jeesl.factory.builder.module.CalendarFactoryBuilder;
import org.jeesl.interfaces.bean.module.JeeslAppCalendarBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAppCalendarBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
											CALENDAR extends JeeslCalendar<ZONE,CT>,
											ZONE extends JeeslCalendarZone<L,D>,
											CT extends JeeslCalendarScope<L,D,CT,?>,
											ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
											IT extends JeeslCalendarItemType<L,D,?,IT,?>,
											USER extends JeeslSimpleUser>
		implements JeeslAppCalendarBean<L,D,CALENDAR,ZONE,CT,ITEM,IT,USER>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAppCalendarBean.class);
	
	@SuppressWarnings("unused")
	private JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fCalendar;
	private final CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fbCalendar;
	
	private final Comparator<ZONE> cpZone;
	
	private final List<ZONE> zones; @Override public List<ZONE> getZones() {return zones;}
	
	private ZONE zoneDefault; @Override public ZONE getZoneDefault() {return zoneDefault;}
	private long timeLineWeekday; public long getTimeLineWeekday() {return timeLineWeekday;} 
	private long timeLineDecade; public long getTimeLineDecade() {return timeLineDecade;}
	
	public AbstractAppCalendarBean(final CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fbCalendar)
	{
		this.fbCalendar=fbCalendar;
		cpZone = (new TimeZoneComparator<L,D,CALENDAR,ZONE,CT,ITEM,IT>()).factory(TimeZoneComparator.Type.offset);
		zones = new ArrayList<>();
		
		timeLineWeekday =   1000000000l;
		timeLineDecade  = 300000000000l;
	}
	
	public void postConstructTimeZone(JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,CT,ITEM,IT,USER> fCalendar)
	{
		this.fCalendar=fCalendar;

		reloadZones(fCalendar);	
	}
	
	@Override public void reloadZones(JeeslFacade facade)
	{
		zones.clear();
		zones.addAll(facade.all(fbCalendar.getClassZone()));
		Collections.sort(zones,cpZone);
		
		zoneDefault = null;
		for(ZONE z : zones)
		{
			if(isDefaultZone(z.getCode())) {zoneDefault=z;}
		}
	}
	
	protected abstract boolean isDefaultZone(String code);
	
}