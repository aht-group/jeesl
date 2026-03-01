package org.jeesl.factory.ejb.module.calendar;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import org.exlp.util.io.StringUtil;
import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.controller.processor.TimeZoneProcessor;
import org.jeesl.interfaces.model.module.calendar.JeeslCalendarZone;
import org.jeesl.test.JeeslBootstrap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEjbTimeZoneFactory extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestEjbTimeZoneFactory.class);
	
	@Before public void line(){logger.debug(StringUtil.stars());}
	
	@Test public void pre()
    {	
		TimeZone tz = TimeZone.getDefault();
		logger.debug(tz.getID());
		logger.debug(tz.getDisplayName());
		
		for(String s : TimeZone.getAvailableIDs())
		{
			logger.trace(s);
		}
    }
	
	@Test public void wrongId()
	{
		Assert.assertFalse(EjbTimeZoneFactory.supportedCode("Europe/Berlin222"));
		Assert.assertTrue(EjbTimeZoneFactory.supportedCode(JeeslCalendarZone.tzBerlin));
	}
	
//	@Test public void timezone()
//	{
//		Date dNow = new Date();
//		Date dUtc = EjbTimeZoneFactory.toUtc(dNow,JeeslCalendarTimeZone.tzBerlin);
//		
//		logger.info(dNow.toString());
//		logger.info(dUtc.toString());
//	}
	
	@Test public void utcToZone()
	{
		logger.debug("Projecting UTC to Berlin");
		Date dNow = new Date();
		Date dProject = TimeZoneProcessor.project(dNow,JeeslCalendarZone.tzUtc,JeeslCalendarZone.tzBerlin);
		
		logger.info("Now "+dNow.toString());
		logger.info("Prj "+dProject.toString());
	}
	
	@Test public void toUtc()
	{
		logger.debug("Projecting Berlin To UTC");
		Date dNow = new Date();
		Date dProject = TimeZoneProcessor.project(dNow,JeeslCalendarZone.tzBerlin,JeeslCalendarZone.tzUtc);
		
		logger.debug("Now "+dNow.toString());
		logger.debug("Prj "+dProject.toString());
		
		java.time.LocalDateTime ldt = java.time.LocalDateTime.now();
		ZoneOffset offset =  ZoneId.systemDefault().getRules().getOffset(ldt);
		java.time.LocalDateTime utc = OffsetDateTime.of(ldt,offset).withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
		
		logger.info("LDT: "+ldt.toString());
		logger.info("UTC:"+utc.toString());
	}
	
	@Test public void offset()
	{
		java.time.LocalDateTime ldt = java.time.LocalDateTime.now();
		ZoneId zone = ZoneId.systemDefault();
		ZoneOffset offset = zone.getRules().getOffset(ldt);
		OffsetDateTime odt = OffsetDateTime.of(ldt,offset);
		
		logger.info(ldt.toString());
		logger.info(odt.toString());
		logger.info(odt.getOffset().toString());
		
		
		
	}
	
	
	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		TestEjbTimeZoneFactory cli = new TestEjbTimeZoneFactory();
//		cli.timezone();
		cli.toUtc();
	}
}