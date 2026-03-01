package org.jeesl.factory.ejb.module.calendar;

import java.time.LocalDateTime;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCalItemFactory<CAL extends JeeslCalendar<ZONE,?>,
								ZONE extends JeeslCalendarZone<?,?>,
								ITEM extends JeeslCalendarItem<CAL,ZONE,IT,?>,
								IT extends JeeslCalendarItemType<?,?,?,IT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbCalItemFactory.class);
	
	private final Class<ZONE> cZone;
	private final Class<ITEM> cItem;
	private final Class<IT> cItemType;
    
	public EjbCalItemFactory(Class<ZONE> cZone, Class<ITEM> cItem, Class<IT> cItemType)
	{
		this.cZone = cZone;
		this.cItem = cItem;
		this.cItemType = cItemType;
	}
	
	public ITEM build(CAL calendar, IT type, String title, LocalDateTime localStart, LocalDateTime localEnd)
	{
		ITEM ejb = null;
		try
		{
			ejb = cItem.newInstance();
			ejb.setCalendar(calendar);
			ejb.setType(type);
			ejb.setTitle(title);
			ejb.setLocalStart(localStart);
			ejb.setLocalEnd(localEnd);
			ejb.setAllDay(true);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void converter(JeeslFacade facade, ITEM item)
	{
		item.setType(facade.find(cItemType,item.getType()));
		item.setStartZone(facade.find(cZone,item.getStartZone()));
		item.setEndZone(facade.find(cZone,item.getEndZone()));
	}
}