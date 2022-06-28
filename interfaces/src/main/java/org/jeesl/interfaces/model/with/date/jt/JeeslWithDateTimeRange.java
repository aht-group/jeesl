package org.jeesl.interfaces.model.with.date.jt;

import java.time.LocalDateTime;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithDateTimeRange extends EjbWithId
{
	public LocalDateTime getStartDate();
	public void setStartDate(LocalDateTime startDate);
	
	public LocalDateTime getEndDate();
	public void setEndDate(LocalDateTime endDate);
}