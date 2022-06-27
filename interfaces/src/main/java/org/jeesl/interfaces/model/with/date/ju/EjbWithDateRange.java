package org.jeesl.interfaces.model.with.date.ju;

import java.util.Date;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithDateRange extends EjbWithId
{
	public Date getStartDate();
	public void setStartDate(Date startDate);
	
	public Date getEndDate();
	public void setEndDate(Date endDate);
}