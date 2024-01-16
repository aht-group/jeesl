package org.jeesl.interfaces.bean.op;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;

import net.sf.ahtutils.interfaces.controller.handler.op.user.OpUserSelectionHandler;

public interface OpUserBean <USER extends JeeslUser<?>>
{
	OpUserSelectionHandler<USER> getOpUserHandler();
	void selectOpUser(USER user) throws JeeslLockingException, JeeslConstraintViolationException;
}