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
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCalendarTimeZoneBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CALENDAR extends JeeslCalendar<ZONE,CT>,
											ZONE extends JeeslCalendarTimeZone<L,D>,
											CT extends JeeslCalendarType<L,D,CT,?>,
											ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT>,
											IT extends JeeslCalendarItemType<L,D,?,IT,?>>
		extends AbstractAdminBean<L,D,LOC>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractCalendarTimeZoneBean.class);
	
	private JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT> fCalendar;
	private JeeslAppCalendarBean<L,D,CALENDAR,ZONE,CT,ITEM,IT> bCalendar;
	
	private final CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar;
	private EjbTimeZoneFactory<ZONE,ITEM> efZone;
	
	private ZONE zone; public ZONE getZone() {return zone;} public void setZone(ZONE zone) {this.zone = zone;}
	
	public AbstractCalendarTimeZoneBean(final CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar)
	{
		super(fbCalendar.getClassL(),fbCalendar.getClassD());
		this.fbCalendar=fbCalendar;
	}
	
	public void postConstructTimeZone(JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT> fCalendar,
										JeeslAppCalendarBean<L,D,CALENDAR,ZONE,CT,ITEM,IT> bCalendar,
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
		zone.setName(efLang.createEmpty(langs));
	}
	
	public void saveZone() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(EjbTimeZoneFactory.supportedCode(zone.getCode()))
		{
			zone = fCalendar.save(zone);
			bCalendar.reloadZones(fCalendar);
			bMessage.growlSuccessSaved();
		}
		else
		{
			bMessage.errorText("TS not supported");
		}
	}
}