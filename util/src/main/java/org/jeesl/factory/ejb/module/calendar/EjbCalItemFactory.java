package org.jeesl.factory.ejb.module.calendar;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCalItemFactory<ZONE extends JeeslCalendarTimeZone<?,?>,
								ITEM extends JeeslCalendarItem<?,ZONE,IT>,
								IT extends JeeslCalendarItemType<?,?,?,IT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbCalItemFactory.class);
	
	private final Class<ZONE> cZone;
	private final Class<IT> cItemType;
    
	public EjbCalItemFactory(Class<ZONE> cZone, Class<IT> cItemType)
	{
		this.cZone = cZone;
		this.cItemType = cItemType;
	}
	
	public void converter(JeeslFacade facade, ITEM item)
	{
		item.setType(facade.find(cItemType,item.getType()));
		item.setStartZone(facade.find(cZone,item.getStartZone()));
		item.setEndZone(facade.find(cZone,item.getEndZone()));
	}
}