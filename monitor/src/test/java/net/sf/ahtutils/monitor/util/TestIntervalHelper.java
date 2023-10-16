package net.sf.ahtutils.monitor.util;

import org.jeesl.exception.processing.UtilsProcessingException;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.MutableDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractUtilsMonitorTest;

public class TestIntervalHelper extends AbstractUtilsMonitorTest
{
	final static Logger logger = LoggerFactory.getLogger(TestIntervalHelper.class);
	
	@Test
	public void day() throws UtilsProcessingException
	{
		logger.debug("DAY");
		Duration d = Duration.standardDays(1);
		DateTime dt = new DateTime();

		MutableDateTime floor = IntervalHelper.floor(dt, d);
		MutableDateTime ceil = IntervalHelper.ceil(dt, d);
		logger.debug("floor: "+floor);
		logger.debug("ceil:  "+ceil);
		
		Assertions.assertEquals(dt.getYear(), floor.getYear());
		Assertions.assertEquals(dt.getMonthOfYear(), floor.getMonthOfYear());
		Assertions.assertEquals(dt.getDayOfMonth(), floor.getDayOfMonth());
		Assertions.assertEquals(0, floor.getHourOfDay());
		Assertions.assertEquals(0, floor.getMinuteOfHour());
		Assertions.assertEquals(0, floor.getSecondOfMinute());
		Assertions.assertEquals(0, floor.getMillisOfSecond());
	}
	
	@Test
	public void hour() throws UtilsProcessingException
	{
		logger.debug("HOUR");
		Duration d = Duration.standardHours(1);
		DateTime dt = new DateTime();

		MutableDateTime floor = IntervalHelper.floor(dt, d);
		MutableDateTime ceil = IntervalHelper.ceil(dt, d);
		logger.debug("floor: "+floor);
		logger.debug("ceil:  "+ceil);
		
		Assertions.assertEquals(dt.getYear(), floor.getYear());
		Assertions.assertEquals(dt.getMonthOfYear(), floor.getMonthOfYear());
		Assertions.assertEquals(dt.getDayOfMonth(), floor.getDayOfMonth());
		Assertions.assertEquals(dt.getHourOfDay(), floor.getHourOfDay());
		Assertions.assertEquals(0, floor.getMinuteOfHour());
		Assertions.assertEquals(0, floor.getSecondOfMinute());
		Assertions.assertEquals(0, floor.getMillisOfSecond());
	}
	
	@Test
	public void minute() throws UtilsProcessingException
	{
		logger.debug("MINUTE");
		Duration d = Duration.standardMinutes(1);
		DateTime dt = new DateTime();

		MutableDateTime floor = IntervalHelper.floor(dt, d);
		MutableDateTime ceil = IntervalHelper.ceil(dt, d);
		logger.debug("floor: "+floor);
		logger.debug("ceil:  "+ceil);
		
		Assertions.assertEquals(dt.getYear(), floor.getYear());
		Assertions.assertEquals(dt.getMonthOfYear(), floor.getMonthOfYear());
		Assertions.assertEquals(dt.getDayOfMonth(), floor.getDayOfMonth());
		Assertions.assertEquals(dt.getHourOfDay(), floor.getHourOfDay());
		Assertions.assertEquals(dt.getMinuteOfHour(), floor.getMinuteOfHour());
		Assertions.assertEquals(0, floor.getSecondOfMinute());
		Assertions.assertEquals(0, floor.getMillisOfSecond());
	}
	
	@Test
	public void second() throws UtilsProcessingException
	{
		logger.debug("SECOND");
		Duration d = Duration.standardSeconds(1);
		DateTime dt = new DateTime();

		MutableDateTime floor = IntervalHelper.floor(dt, d);
		MutableDateTime ceil = IntervalHelper.ceil(dt, d);
		logger.debug("floor: "+floor);
		logger.debug("ceil:  "+ceil);
		
		Assertions.assertEquals(dt.getYear(), floor.getYear());
		Assertions.assertEquals(dt.getMonthOfYear(), floor.getMonthOfYear());
		Assertions.assertEquals(dt.getDayOfMonth(), floor.getDayOfMonth());
		Assertions.assertEquals(dt.getHourOfDay(), floor.getHourOfDay());
		Assertions.assertEquals(dt.getMinuteOfHour(), floor.getMinuteOfHour());
		Assertions.assertEquals(dt.getSecondOfMinute(), floor.getSecondOfMinute());
		Assertions.assertEquals(0, floor.getMillisOfSecond());
	}
	
	// DAYS
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationDayHour() throws UtilsProcessingException
	{
		Duration d = Duration.standardDays(1);
		d = d.plus(Duration.standardHours(1));
		IntervalHelper.floor(new DateTime(),d);
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationDayMinute() throws UtilsProcessingException
	{
		Duration d = Duration.standardDays(1);
		d = d.plus(Duration.standardMinutes(1));
		IntervalHelper.floor(new DateTime(),d);
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationDaySecond() throws UtilsProcessingException
	{
		Duration d = Duration.standardDays(1);
		d = d.plus(Duration.standardSeconds(1));
		IntervalHelper.floor(new DateTime(),d);
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationDays2() throws UtilsProcessingException
	{
		IntervalHelper.floor(new DateTime(),Duration.standardDays(2));
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationDayMilli() throws UtilsProcessingException
	{
		Duration d = Duration.standardDays(1);
		d = d.plus(1);
		IntervalHelper.floor(new DateTime(),d);
	}
	
	// HOUR
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationHourMinute() throws UtilsProcessingException
	{
		Duration d = Duration.standardHours(1);
		d = d.plus(Duration.standardMinutes(1));
		IntervalHelper.floor(new DateTime(),d);
	}
	
	@Test @Disabled// (expected=UtilsProcessingException.class)
	public void durationHourSecond() throws UtilsProcessingException
	{
		Duration d = Duration.standardHours(1);
		d = d.plus(Duration.standardSeconds(1));
		IntervalHelper.floor(new DateTime(),d);
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationHourMilli() throws UtilsProcessingException
	{
		Duration d = Duration.standardHours(1);
		d = d.plus(1);
		IntervalHelper.floor(new DateTime(),d);
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void hour2() throws UtilsProcessingException
	{
		IntervalHelper.floor(new DateTime(),Duration.standardHours(2));
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void hour5() throws UtilsProcessingException
	{
		IntervalHelper.floor(new DateTime(),Duration.standardHours(5));
	}
	
	// Minute
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationMinuteSecond() throws UtilsProcessingException
	{
		Duration d = Duration.standardMinutes(1);
		d = d.plus(Duration.standardSeconds(1));
		IntervalHelper.floor(new DateTime(),d);
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationMinuteMilli() throws UtilsProcessingException
	{
		Duration d = Duration.standardMinutes(1);
		d = d.plus(1);
		IntervalHelper.floor(new DateTime(),d);
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void minute58() throws UtilsProcessingException
	{
		IntervalHelper.floor(new DateTime(),Duration.standardMinutes(58));
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void minute2() throws UtilsProcessingException
	{
		IntervalHelper.floor(new DateTime(),Duration.standardMinutes(2));
	}
	
	// Second
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void durationSecondMilli() throws UtilsProcessingException
	{
		Duration d = Duration.standardSeconds(1);
		d = d.plus(1);
		IntervalHelper.floor(new DateTime(),d);
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void second58() throws UtilsProcessingException
	{
		IntervalHelper.floor(new DateTime(),Duration.standardSeconds(58));
	}
	
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void second2() throws UtilsProcessingException
	{
		IntervalHelper.floor(new DateTime(),Duration.standardSeconds(2));
	}
	
	// Milliseconds
	@Test @Disabled //(expected=UtilsProcessingException.class)
	public void milli1() throws UtilsProcessingException
	{
		Duration d = Duration.standardSeconds(0);
		d = d.plus(1);
		IntervalHelper.floor(new DateTime(),d);
	}
}