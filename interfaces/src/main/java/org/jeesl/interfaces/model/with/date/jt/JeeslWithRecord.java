package org.jeesl.interfaces.model.with.date.jt;

import java.time.LocalDate;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithRecord extends EjbWithId
{	
	public LocalDate getRecord();
	public void setRecord(LocalDate record);
}