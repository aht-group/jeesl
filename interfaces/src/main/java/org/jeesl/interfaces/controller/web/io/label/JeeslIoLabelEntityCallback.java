package org.jeesl.interfaces.controller.web.io.label;

import java.io.Serializable;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.label.Entity;

public interface JeeslIoLabelEntityCallback extends Serializable
{
	Entity downloadEntity(String code) throws UtilsConfigurationException;
}