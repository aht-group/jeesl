package org.jeesl.interfaces.bean.sb.bean;

import java.io.Serializable;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;

public interface SbToggleBean extends Serializable
{
	void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException;
}