package org.jeesl.interfaces.controller.web.module.rmmv;

import java.io.Serializable;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;

public interface JeeslRmmvElementCallback <MC extends JeeslRmmvModuleConfig<?,?>> extends Serializable
{
	void postConfigSelect(MC config);
	void postConfigSave(MC config) throws JeeslConstraintViolationException, JeeslLockingException;
}