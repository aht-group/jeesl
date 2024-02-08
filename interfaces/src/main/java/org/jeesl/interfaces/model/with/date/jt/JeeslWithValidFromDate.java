package org.jeesl.interfaces.model.with.date.jt;

import java.time.LocalDate;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithValidFromDate extends EjbWithId
{	
//	SQL: validFrom date NOT NULL,
	
	public LocalDate getValidFrom();
	public void setValidFrom(LocalDate validFrom);
}