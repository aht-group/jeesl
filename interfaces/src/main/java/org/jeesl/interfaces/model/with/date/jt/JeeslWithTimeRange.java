package org.jeesl.interfaces.model.with.date.jt;

import java.time.LocalDateTime;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithTimeRange extends EjbWithId
{
	LocalDateTime getStartDate();
	void setStartDate(LocalDateTime startDate);
	
	LocalDateTime getEndDate();
	void setEndDate(LocalDateTime endDate);
}