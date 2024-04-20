package org.jeesl.model.ejb.io.db;

import java.io.Serializable;
import java.time.LocalDate;

public interface JeeslCqDate extends Serializable
{
	public enum Type {DbIsBefore,DbIsBeforeOrEqual,DbIsEqual,DbIsEqualOrAfter,DbIsAfter}
	
	Type getType();
	LocalDate getDate();
	String getPath();
}