package org.jeesl.controller.handler.sb;

import java.time.LocalDate;

import org.jeesl.api.handler.sb.SbDateSelection;
import org.jeesl.api.handler.sb.SbDateSelectionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbDateHandler implements SbDateSelection
{
	final static Logger logger = LoggerFactory.getLogger(SbDateHandler.class);
	private static final long serialVersionUID = 1L;

	private SbDateSelectionBean bean;

	private LocalDate dateFrom; public LocalDate getDateFrom() {return dateFrom;} public void setDateFrom(LocalDate dateFrom) {this.dateFrom = dateFrom;}
	private LocalDate dateTo; public LocalDate getDateTo() {return dateTo;} public void setDateTo(LocalDate dateTo) {this.dateTo = dateTo;}
	
	
	public static SbDateHandler instance(SbDateSelectionBean bean) {return new SbDateHandler(bean);}
	
	public SbDateHandler(SbDateSelectionBean bean)
	{
		this.bean=bean;
	}

	public SbDateHandler initToday()
	{
		dateFrom = LocalDate.now();
		dateTo = LocalDate.now();
		return this;
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