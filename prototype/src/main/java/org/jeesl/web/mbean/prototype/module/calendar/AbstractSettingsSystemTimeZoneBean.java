package org.jeesl.web.mbean.prototype.module.calendar;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslCalendarFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.CalendarFactoryBuilder;
import org.jeesl.factory.ejb.module.calendar.EjbTimeZoneFactory;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendar;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarItem;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarTimeZone;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.util.comparator.ejb.module.TimeZoneComparator;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractSettingsSystemTimeZoneBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CALENDAR extends JeeslCalendar<ZONE,CT>,
											ZONE extends JeeslCalendarTimeZone<L,D>,
											CT extends JeeslCalendarType<L,D,CT,?>,
											ITEM extends JeeslCalendarItem<CALENDAR,ZONE,IT>,
											IT extends JeeslStatus<L,D,IT>>
		extends AbstractAdminBean<L,D,LOC>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSettingsSystemTimeZoneBean.class);
	
	private JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT> fCalendar;
	private final CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar;
	
	private List<ZONE> zones; public List<ZONE> getZones() {return zones;}
	
	private ZONE zone; public ZONE getZone() {return zone;} public void setZone(ZONE zone) {this.zone = zone;}
	
	private Comparator<ZONE> cpZone;
	private EjbTimeZoneFactory<ZONE,ITEM> efZone;
	
	public AbstractSettingsSystemTimeZoneBean(final CalendarFactoryBuilder<L,D,CALENDAR,ZONE,CT,ITEM,IT> fbCalendar)
	{
		super(fbCalendar.getClassL(),fbCalendar.getClassD());
		this.fbCalendar=fbCalendar;
	}
	
	public void postConstructTimeZone(JeeslCalendarFacade<L,D,CALENDAR,ZONE,CT,ITEM,IT> fCalendar, JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fCalendar=fCalendar;

		efZone = fbCalendar.ejbZone();
		
		cpZone = (new TimeZoneComparator<L,D,CALENDAR,ZONE,CT,ITEM,IT>()).factory(TimeZoneComparator.Type.offset);
		reload();	
	}
	
	
	private void reload()
	{
		zones  = fCalendar.all(fbCalendar.getClassZone());
		Collections.sort(zones,cpZone);
	}
	
	public void selectZone() throws JeeslNotFoundException
	{
		zone = fCalendar.find(fbCalendar.getClassZone(),zone);
	}
	
	public void addZone() throws JeeslNotFoundException
	{
		zone = efZone.build();
		zone.setName(efLang.createEmpty(langs));
	}
	
	public void saveZone() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(EjbTimeZoneFactory.supportedCode(zone.getCode()))
		{
			zone = fCalendar.save(zone);
			updatePerformed();
			bMessage.growlSuccessSaved();
		}
		else
		{
			bMessage.errorText("TS not supported");
		}

		reload();
	}
	
	protected void updatePerformed(){}
}