package org.jeesl.factory.builder.module;

import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.controller.handler.module.calendar.JeeslCalendarHandler;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.calendar.EjbCalCalendarFactory;
import org.jeesl.factory.ejb.module.calendar.EjbCalItemFactory;
import org.jeesl.factory.ejb.module.calendar.EjbTimeZoneFactory;
import org.jeesl.factory.txt.module.calendar.TxtCalendarItemFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalendarFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
									YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
									CAL extends JeeslCalendar<ZONE,CT>,
									ZONE extends JeeslCalendarZone<L,D>,
									CT extends JeeslCalendarScope<L,D,CT,?>,
									ITEM extends JeeslCalendarItem<CAL,ZONE,IT,USER>,
									IT extends JeeslCalendarItemType<L,D,?,IT,?>,
									USER extends JeeslSimpleUser>
	extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(CalendarFactoryBuilder.class);
	
	private final Class<CAL> cCalendar; public Class<CAL> getClassCalendar()	{return cCalendar;}
	private final Class<ZONE> cZone; public Class<ZONE> getClassZone()	{return cZone;}
	private final Class<CT> cCalType; public Class<CT> getClassCalType()	{return cCalType;}
	private final Class<ITEM> cItem; public Class<ITEM> getClassItem()	{return cItem;}
	private final Class<IT> cItemType; public Class<IT> getClassItemType()	{return cItemType;}

	public CalendarFactoryBuilder(final Class<L> cL,final Class<D> cD, Class<CAL> cCalendar, Class<ZONE> cZone, Class<CT> cCalType, final Class<ITEM> cItem, final Class<IT> cItemType)
	{       
		super(cL,cD);
		this.cCalendar = cCalendar;
        this.cZone = cZone;
        this.cCalType = cCalType;
        this.cItem=cItem;
        this.cItemType = cItemType;
	}
	
	public EjbCalCalendarFactory<CAL,ZONE,CT> ejbCalendar(){return new EjbCalCalendarFactory<>(cCalendar,cZone,cCalType);}
	public EjbTimeZoneFactory<ZONE,ITEM> ejbZone(){return new EjbTimeZoneFactory<>(cZone);}
	public EjbCalItemFactory<CAL,ZONE,ITEM,IT> ejbItem(){return new EjbCalItemFactory<>(cZone,cItem,cItemType);}
	
	public TxtCalendarItemFactory<L,D,CAL,ZONE,CT,ITEM,IT> txtItem() {return new TxtCalendarItemFactory<L,D,CAL,ZONE,CT,ITEM,IT>();}
	
	public JeeslCalendarHandler<L,D,YEAR,CAL,ZONE,CT,ITEM,IT,USER> itemHandler(final JeeslCalendarFacade<L,D,YEAR,CAL,ZONE,CT,ITEM,IT,USER> fCalendar){return new JeeslCalendarHandler<>(fCalendar,this);}
}