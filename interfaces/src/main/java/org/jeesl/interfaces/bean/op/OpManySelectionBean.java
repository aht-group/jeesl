package org.jeesl.interfaces.bean.op;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.controller.handler.op.OpSelectionHandler;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface OpManySelectionBean
{
//	void x();
	void callbackOpSelection(OpSelectionHandler handler, EjbWithId ejb) throws JeeslLockingException, JeeslConstraintViolationException;
}