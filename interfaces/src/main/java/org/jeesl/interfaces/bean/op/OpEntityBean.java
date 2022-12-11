package org.jeesl.interfaces.bean.op;

import java.io.Serializable;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.controller.handler.op.OpEntityHandler;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface OpEntityBean extends Serializable
{
	void addOpEntity(OpEntityHandler handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException;
	void rmOpEntity(OpEntityHandler handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException;
}