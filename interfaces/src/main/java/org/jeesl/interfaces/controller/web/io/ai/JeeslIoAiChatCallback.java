package org.jeesl.interfaces.controller.web.io.ai;

import java.io.Serializable;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;

public interface JeeslIoAiChatCallback extends Serializable
{
	void chatRequestCompleted() throws JeeslConstraintViolationException, JeeslLockingException;
}