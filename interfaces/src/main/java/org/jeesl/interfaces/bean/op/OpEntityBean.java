package org.jeesl.interfaces.bean.op;

import java.io.Serializable;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.controller.handler.op.OpSelectionHandler;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface OpEntityBean extends Serializable
{
	void addOpEntity(OpSelectionHandler handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException;
	void rmOpEntity(OpSelectionHandler handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException;
}