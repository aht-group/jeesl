package org.jeesl.interfaces.model.with.date.jt;

import java.time.LocalDate;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithDateRange extends EjbWithId
{
	public LocalDate getStartDate();
	public void setStartDate(LocalDate startDate);
	
	public LocalDate getEndDate();
	public void setEndDate(LocalDate endDate);
}