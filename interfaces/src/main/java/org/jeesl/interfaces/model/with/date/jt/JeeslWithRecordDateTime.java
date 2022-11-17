package org.jeesl.interfaces.model.with.date.jt;

import java.time.LocalDateTime;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithRecordDateTime extends EjbWithId
{	
	public LocalDateTime getRecord();
	public void setRecord(LocalDateTime record);
}