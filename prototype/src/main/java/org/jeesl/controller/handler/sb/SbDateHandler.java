package org.jeesl.controller.handler.sb;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.jeesl.interfaces.bean.sb.bean.SbDateSelectionBean;
import org.jeesl.interfaces.bean.sb.handler.SbDateSelection;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbDateHandler implements SbDateSelection
{
	final static Logger logger = LoggerFactory.getLogger(SbDateHandler.class);
	private static final long serialVersionUID = 1L;

	private SbDateSelectionBean bean;
	private LocalDate dateFrom; public LocalDate getDateFrom() {return dateFrom;} public void setDateFrom(LocalDate dateFrom) {this.dateFrom = dateFrom;}
	private LocalDate dateTo; public LocalDate getDateTo() {return dateTo;} public void setDateTo(LocalDate dateTo) {this.dateTo = dateTo;}

	private LocalDate minFrom; public LocalDate getMinFrom() {return minFrom;} public void setMinFrom(LocalDate minFrom) {this.minFrom = minFrom;}
	private LocalDate minTo; public LocalDate getMinTo() {return minTo;} public void setMinTo(LocalDate minTo) {this.minTo = minTo;}

	private LocalDate maxFrom; public LocalDate getMaxFrom() {return maxFrom;} public void setMaxFrom(LocalDate maxFrom) {this.maxFrom = maxFrom;}
	private LocalDate maxTo; public LocalDate getMaxTo() {return maxTo;} public void setMaxTo(LocalDate maxTo) {this.maxTo = maxTo;}
	
	public LocalDateTime getDateFromLdt() {if(dateFrom==null) {return null;} else {return dateFrom.atStartOfDay();}}
	public LocalDateTime getDateToLdt() {if(dateTo==null) {return null;} else {return dateTo.atStartOfDay().plusDays(1);}}
	
	public static SbDateHandler instance(SbDateSelectionBean bean) {return new SbDateHandler(bean);}
	private SbDateHandler(SbDateSelectionBean bean)
	{
		this.bean=bean;
	}

	public SbDateHandler initToday()
	{
		dateFrom = LocalDate.now();
		dateTo = LocalDate.now();
		return this;
	}

	public SbDateHandler initMonths(int before, int after)
	{
		dateFrom = LocalDate.now().withDayOfMonth(1).minusMonths(before);
		dateTo = LocalDate.now().withDayOfMonth(1).plusMonths(after).plusMonths(1).minusDays(1);
		return this;
	}
	
	public SbDateHandler initWeeks(int before, int after)
	{
		dateFrom = LocalDate.now().minusWeeks(before);
		dateTo = LocalDate.now().plusWeeks(after);
		return this;
	}
	
	public void shiftToFirstLastDayofMonth()
	{
		dateFrom = dateFrom.withDayOfMonth(1);
		dateTo = dateTo.withDayOfMonth(1).plusMonths(1).minusDays(1);
	}
	
	
	public void dateChanged()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("dateChanged: ");
		if(dateFrom!=null) {sb.append(dateFrom.toString());}
		sb.append(" -> ");
		if(dateTo!=null) {sb.append(dateTo.toString());}
		logger.info(sb.toString());
		if(bean!=null){bean.callbackDateChanged(this);}
	}
	
}