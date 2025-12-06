package org.jeesl.web.rest.module;

import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.api.rest.rs.module.calendar.JeeslCalendarRest;
import org.jeesl.factory.builder.module.CalendarFactoryBuilder;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCalendarRestHandler <L extends JeeslLang, D extends JeeslDescription,
								YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
								CALENDAR extends JeeslCalendar<ZONE,SCOPE>,
								ZONE extends JeeslCalendarTimeZone<L,D>,
								SCOPE extends JeeslCalendarScope<L,D,SCOPE,?>,
								ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
								IT extends JeeslCalendarItemType<L,D,?,IT,?>,
								USER extends JeeslSimpleUser
								>
					extends AbstractJeeslRestHandler<L,D>
					implements JeeslCalendarRest
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCalendarRestHandler.class);
	
	private final CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fbCalendar;
	private final JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fCalendar;
	
	private JeeslCalendarRestHandler(final CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fbCalendar,
									 final JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fCalendar)
	{
		super(fCalendar,fbCalendar.getClassL(),fbCalendar.getClassD());
		this.fbCalendar=fbCalendar;
		this.fCalendar=fCalendar;
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
					CALENDAR extends JeeslCalendar<ZONE,SCOPE>,
					ZONE extends JeeslCalendarTimeZone<L,D>,
					SCOPE extends JeeslCalendarScope<L,D,SCOPE,?>,
					ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
					IT extends JeeslCalendarItemType<L,D,?,IT,?>,
					USER extends JeeslSimpleUser>
			JeeslCalendarRestHandler<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER>
			instance(final CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fbCalendar,
					JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fCalendar)
	{
		return new JeeslCalendarRestHandler<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER>(fbCalendar,fCalendar);
	}
	
//	@Override public Container exportCalendarType() {return xfContainer.build(fCalendar.allOrderedPosition(cType));}
//	@Override public Container exportCalendarItemType() {return xfContainer.build(fCalendar.allOrderedPosition(cItemType));}

//	@Override public DataUpdate importCalendarType(Container container){return importStatus(cType,container,null);}
//	@Override public DataUpdate importCalendarItemType(Container container){return importStatus(cItemType,container,null);}
}