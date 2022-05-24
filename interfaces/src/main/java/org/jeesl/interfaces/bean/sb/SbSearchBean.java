package org.jeesl.interfaces.bean.sb;

import java.io.Serializable;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.controller.handler.JeeslHandler;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface SbSearchBean extends Serializable
{
	void selectSbSearch(JeeslHandler handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException;
	
	@Deprecated void triggerSbSearch();
}