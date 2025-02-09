package org.jeesl.interfaces.controller.web.io.label;

import java.io.Serializable;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.rest.system.JeeslSystemRestInterface;
import org.jeesl.model.xml.io.label.Entity;

public interface JeeslIoLabelEntityCallback extends Serializable
{
	JeeslSystemRestInterface rest(String code);
	Entity downloadEntity(String code) throws UtilsConfigurationException;
}