package org.jeesl.web.mbean.prototype.module.calendar;

import java.io.Serializable;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.CalendarFactoryBuilder;
import org.jeesl.factory.ejb.module.calendar.EjbTimeZoneFactory;
import org.jeesl.interfaces.bean.module.JeeslAppCalendarBean;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItemType;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarScope;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarYear;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCalendarTimeZoneBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											YEAR extends JeeslCalendarYear<?,?,YEAR,?>,
											CALENDAR extends JeeslCalendar<ZONE,SCOPE>,
											ZONE extends JeeslCalendarTimeZone<L,D>,
											SCOPE extends JeeslCalendarScope<L,D,SCOPE,?>,
											ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT,USER>,
											IT extends JeeslCalendarItemType<L,D,?,IT,?>,
											USER extends JeeslSimpleUser>
		extends AbstractAdminBean<L,D,LOC>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractCalendarTimeZoneBean.class);
	
	private JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fCalendar;
	private JeeslAppCalendarBean<L,D,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> bCalendar;
	
	private final CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fbCalendar;
	private EjbTimeZoneFactory<ZONE,ITEM> efZone;
	
	private ZONE zone; public ZONE getZone() {return zone;} public void setZone(ZONE zone) {this.zone = zone;}
	
	public AbstractCalendarTimeZoneBean(final CalendarFactoryBuilder<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fbCalendar)
	{
		super(fbCalendar.getClassL(),fbCalendar.getClassD());
		this.fbCalendar=fbCalendar;
	}
	
	public void postConstructTimeZone(JeeslCalendarFacade<L,D,YEAR,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> fCalendar,
										JeeslAppCalendarBean<L,D,CALENDAR,ZONE,SCOPE,ITEM,IT,USER> bCalendar,
										JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fCalendar=fCalendar;
		this.bCalendar=bCalendar;

		efZone = fbCalendar.ejbZone();
		
		if(bCalendar.getZoneDefault()!=null) {zone = fCalendar.find(fbCalendar.getClassZone(),bCalendar.getZoneDefault());}
	}
	
	
	public void selectZone()
	{
		zone = fCalendar.find(fbCalendar.getClassZone(),zone);
	}
	
	public void addZone()
	{
		zone = efZone.build();
		zone.setName(efLang.buildEmpty(lp.getLocales()));
	}
	
	public void saveZone() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(EjbTimeZoneFactory.supportedCode(zone.getCode()))
		{
			zone = fCalendar.save(zone);
			bCalendar.reloadZones(fCalendar);
			bMessage.growlSaved(zone);
		}
		else
		{
			bMessage.error(null,"TS not supported");
		}
	}
}