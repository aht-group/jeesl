package org.jeesl.factory.ejb.module.calendar;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCalCalendarFactory<CAL extends JeeslCalendar<ZONE,CT>,
									ZONE extends JeeslCalendarTimeZone<?,?>,
									CT extends JeeslCalendarType<?,?,CT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbCalCalendarFactory.class);
	
	private final Class<CAL> cCalendar;
	private final Class<ZONE> cZone;
	private final Class<CT> cType;
    
	public EjbCalCalendarFactory(Class<CAL> cCalendar, Class<ZONE> cZone, Class<CT> cType)
	{
		this.cCalendar = cCalendar;
		this.cZone = cZone;
		this.cType = cType;
	}
	
	public CAL build(CT type)
	{
		CAL ejb = null;
		try
		{
			ejb = cCalendar.newInstance();
			ejb.setType(type);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void converter(JeeslFacade facade, CAL item)
	{
		item.setType(facade.find(cType,item.getType()));
//		item.setStartZone(facade.find(cZone,item.getStartZone()));
	}
}