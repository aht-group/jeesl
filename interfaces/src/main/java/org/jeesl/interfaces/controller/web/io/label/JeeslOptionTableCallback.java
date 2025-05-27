package org.jeesl.interfaces.controller.web.io.label;

import java.io.Serializable;

import org.jeesl.interfaces.rest.system.JeeslSystemRestInterface;

public interface JeeslOptionTableCallback extends Serializable
{
	JeeslSystemRestInterface rest(String code);
	void callbackStatusSaved(Object ejb);
}