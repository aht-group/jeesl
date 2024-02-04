package org.jeesl.controller.handler.sb;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.exlp.util.system.DateUtil;
import org.jeesl.api.handler.sb.SbDateIntervalSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbDateIntervalHandler implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(SbDateIntervalHandler.class);
	private static final long serialVersionUID = 1L;

	private SbDateIntervalSelection bean;
	private boolean enforceStartOfDay; public void setEnforceStartOfDay(boolean enforceStartOfDay) {this.enforceStartOfDay = enforceStartOfDay;}

	private Date date1; public Date getDate1() {return date1;}
	private Date date2; public Date getDate2() {return date2;}
	
	//Date 3 is (rarely used as a maxDate)
	private Date date3; public Date getDate3() {return date3;}
	
	private LocalDateTime localDate1; public LocalDateTime getLocalDate1() {return localDate1;}
	private LocalDateTime localDate2; public LocalDateTime getLocalDate2() {return localDate2;}
	private LocalDateTime localDate3; public LocalDateTime getLocalDate3() {return localDate3;}

	public SbDateIntervalHandler(){this(null);}

	public SbDateIntervalHandler(SbDateIntervalSelection bean)
	{
		this.bean=bean;
		enforceStartOfDay = true;
	}

	public static SbDateIntervalHandler build() {return new SbDateIntervalHandler();}
	public SbDateIntervalHandler enforceStartOfDay(boolean enforce) {this.setEnforceStartOfDay(enforce);return this;}

	public void initToday()
	{
		LocalDateTime ldt = LocalDateTime.now();
		setDate1(DateUtil.toDate(ldt));
		setDate2(DateUtil.toDate(ldt));
	}

	public void initMonthsToNow(int months)
	{
		LocalDateTime ldt = LocalDateTime.now();
		setDate1(DateUtil.toDate(ldt.minusMonths(months)));
		setDate2(DateUtil.toDate(ldt));
	}

	public void initMonths(int from, int to)
	{
		LocalDateTime ldt = LocalDateTime.now();
		setDate1(DateUtil.toDate(ldt.minusMonths(from)));
		setDate2(DateUtil.toDate(ldt.plusMonths(to)));
	}

	public void initWeeksToNow(int weeks)
	{
		LocalDateTime ldt = LocalDateTime.now();
		setDate1(DateUtil.toDate(ldt.minusWeeks(weeks)));
		setDate2(DateUtil.toDate(ldt));
	}

	public void initDaysToNow(int days)
	{
		LocalDateTime ldt = LocalDateTime.now();
		setDate1(DateUtil.toDate(ldt.minusDays(days)));
		setDate2(DateUtil.toDate(ldt));
	}

	public void initWeeks(int minus, int plus)
	{
		LocalDateTime ldt = LocalDateTime.now();
		setDate1(DateUtil.toDate(ldt.minusWeeks(minus)));
		setDate2(DateUtil.toDate(ldt.plusWeeks(plus)));
	}

	public void setDate1(Date date1)
	{
		if(enforceStartOfDay)
		{
			LocalDate ld = DateUtil.toLocalDate(date1);
			this.date1 = DateUtil.toDate(ld.atStartOfDay());
		}
		else {this.date1 = date1;}
		this.localDate1 = Instant.ofEpochMilli(this.date1.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public void setDate2(Date date2)
	{
		if(enforceStartOfDay)
		{
			LocalDate ld = DateUtil.toLocalDate(date2);
			this.date2 = DateUtil.toDate(ld.atStartOfDay());
		}
		else {this.date2 = date2;}
		this.localDate2 =  Instant.ofEpochMilli(this.date2.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public void setDate3(Date date3)
	{
		if(enforceStartOfDay)
		{
			LocalDate ld = DateUtil.toLocalDate(date3);
			this.date3 = DateUtil.toDate(ld.atStartOfDay());
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
		LocalDate ld1 = DateUtil.toLocalDate(date1);
		date1 = DateUtil.toDate(ld1.withDayOfMonth(1).atStartOfDay());

		LocalDate ld2 = DateUtil.toLocalDate(date2);
		date2 = DateUtil.toDate(ld2.withDayOfMonth(1).plusMonths(1).minusDays(1).atStartOfDay());
	}

	public Date toDate2Plus1()
	{
		LocalDateTime ldt = DateUtil.toLocalDateTime(date2);
		return DateUtil.toDate(ldt.plusDays(1));
	}
}