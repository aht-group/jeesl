package org.jeesl.interfaces.model.with.date.jt;

import java.time.LocalDate;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithValidUntilDate extends EjbWithId
{	
	public LocalDate getValidUntil();
	public void setValidUntil(LocalDate validUntil);
}