package org.jeesl.interfaces.bean.th;

import java.io.Serializable;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;

public interface ThMultiFilterBean extends Serializable
{
	void filtered(ThMultiFilter filter) throws JeeslLockingException, JeeslConstraintViolationException;
}