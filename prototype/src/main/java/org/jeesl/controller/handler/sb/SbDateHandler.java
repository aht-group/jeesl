package org.jeesl.controller.handler.sb;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.jeesl.api.handler.sb.SbDateIntervalSelection;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbDateHandler implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(SbDateHandler.class);
	private static final long serialVersionUID = 1L;

	private SbDateIntervalSelection bean;
	private boolean enforceStartOfDay; public void setEnforceStartOfDay(boolean enforceStartOfDay) {this.enforceStartOfDay = enforceStartOfDay;}

	private Date date1; public Date getDate1() {return date1;}
	private Date date2; public Date getDate2() {return date2;}
	private Date date3; public Date getDate3() {return date3;}
	private LocalDateTime localDate1; public LocalDateTime getLocalDate1() {return localDate1;}
	private LocalDateTime localDate2; public LocalDateTime getLocalDate2() {return localDate2;}
	private LocalDateTime localDate3; public LocalDateTime getLocalDate3() {return localDate3;}

	public SbDateHandler(){this(null);}

	public SbDateHandler(SbDateIntervalSelection bean)
	{
		this.bean=bean;
		enforceStartOfDay = true;
	}

	public static SbDateHandler build() {return new SbDateHandler();}
	public SbDateHandler enforceStartOfDay(boolean enforce) {this.setEnforceStartOfDay(enforce);return this;}

	public void initToday()
	{
		DateTime dt = new DateTime();
		setDate1(dt.toDate());
		setDate2(dt.toDate());
	}

	public void initMonthsToNow(int months)
	{
		DateTime dt = new DateTime();
		setDate1(dt.minusMonths(months).toDate());
		setDate2(dt.toDate());
	}

	public void initMonths(int from, int to)
	{
		DateTime dt = new DateTime();
		setDate1(dt.minusMonths(from).toDate());
		setDate2(dt.plusMonths(to).toDate());
	}

	public void initWeeksToNow(int weeks)
	{
		DateTime dt = new DateTime();
		setDate1(dt.minusWeeks(weeks).toDate());
		setDate2(dt.toDate());
	}

	public void initDaysToNow(int days)
	{
		DateTime dt = new DateTime();
		setDate1(dt.minusDays(days).toDate());
		setDate2(dt.toDate());
	}

	public void initWeeks(int minus, int plus)
	{
		DateTime dt = new DateTime();
		setDate1(dt.minusWeeks(minus).toDate());
		setDate2(dt.plusWeeks(plus).toDate());
	}

	public void setDate1(Date date1)
	{
		if(enforceStartOfDay)
		{
			DateTime dt = new DateTime(date1);
			this.date1 = dt.withTimeAtStartOfDay().toDate();
		}
		else {this.date1 = date1;}
		this.localDate1 =  Instant.ofEpochMilli(this.date1.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public void setDate2(Date date2)
	{
		if(enforceStartOfDay)
		{
			DateTime dt = new DateTime(date2);
			this.date2 = dt.withTimeAtStartOfDay().toDate();
		}
		else {this.date2 = date2;}
		this.localDate2 =  Instant.ofEpochMilli(this.date2.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public void setDate3(Date date3)
	{
		if(enforceStartOfDay)
		{
			DateTime dt = new DateTime(date3);
			this.date3 = dt.withTimeAtStartOfDay().toDate();
		}
		else {this.date3 = date3;}
		this.localDate3 =  Instant.ofEpochMilli(this.date3.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public void dateChanged()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("dateChanged: ");
		if(date1!=null) {sb.append(date1.toString());}
		sb.append(" -> ");
		if(date1!=null) {sb.append(date2.toString());}
		logger.info(sb.toString());
		if(bean!=null){bean.callbackDateChanged();}
	}

	public void shiftToFirstLastDayofMonth()
	{
		DateTime dt1 = new DateTime(date1);
		date1 = dt1.withTimeAtStartOfDay().withDayOfMonth(1).toDate();

		DateTime dt2 = new DateTime(date2);
		date2 = dt2.withTimeAtStartOfDay().withDayOfMonth(1).plusMonths(1).minusDays(1).toDate();
	}

	public Date toDate2Plus1()
	{
		DateTime dt2 = new DateTime(date2);
		return dt2.plusDays(1).toDate();
	}
}