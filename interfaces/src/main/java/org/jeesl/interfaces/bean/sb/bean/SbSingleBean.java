package org.jeesl.interfaces.bean.sb.bean;

import java.io.Serializable;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface SbSingleBean extends Serializable
{
	void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException;
}