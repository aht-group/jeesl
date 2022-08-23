package org.jeesl.web.rest.module;

import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.api.rest.module.calendar.JeeslCalendarRestExport;
import org.jeesl.api.rest.module.calendar.JeeslCalendarRestImport;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.web.rest.AbstractJeeslRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.sync.DataUpdate;

public class CalendarRestService <L extends JeeslLang, D extends JeeslDescription,
								CALENDAR extends JeeslCalendar<ZONE,CT>,
								ZONE extends JeeslCalendarTimeZone<L,D>,
								CT extends JeeslCalendarScope<L,D,CT,?>,
								ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
								IT extends JeeslCalendarItemType<L,D,?,IT,?>,
								USER extends JeeslSimpleUser
								>
					extends AbstractJeeslRestService<L,D>
					implements JeeslCalendarRestExport,JeeslCalendarRestImport
{
	final static Logger logger = LoggerFactory.getLogger(CalendarRestService.class);
	
	private final JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT,USER> fCalendar;
	
	private final Class<CT> cType;
	private final Class<IT> cItemType;
	
	private CalendarRestService(JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT,USER> fCalendar,final Class<L> cL, final Class<D> cD, final Class<CT> cType, final Class<IT> cItemType)
	{
		super(fCalendar,cL,cD);
		this.fCalendar=fCalendar;
		
		this.cType=cType;
		this.cItemType=cItemType;
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					CALENDAR extends JeeslCalendar<ZONE,CT>,
					ZONE extends JeeslCalendarTimeZone<L,D>,
					CT extends JeeslCalendarScope<L,D,CT,?>,
					ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
					IT extends JeeslCalendarItemType<L,D,?,IT,?>,
					USER extends JeeslSimpleUser>
			CalendarRestService<L,D,CALENDAR,ZONE,CT,ITEM,IT,USER>
			factory(JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT,USER> fCalendar,final Class<L> cL, final Class<D> cD, final Class<CT> cType, final Class<IT> cItemType)
	{
		return new CalendarRestService<L,D,CALENDAR,ZONE,CT,ITEM,IT,USER>(fCalendar,cL,cD,cType,cItemType);
	}
	
	@Override public Container exportCalendarType() {return xfContainer.build(fCalendar.allOrderedPosition(cType));}
	@Override public Container exportCalendarItemType() {return xfContainer.build(fCalendar.allOrderedPosition(cItemType));}

	@Override public DataUpdate importCalendarType(Container container){return importStatus(cType,container,null);}
	@Override public DataUpdate importCalendarItemType(Container container){return importStatus(cItemType,container,null);}
}