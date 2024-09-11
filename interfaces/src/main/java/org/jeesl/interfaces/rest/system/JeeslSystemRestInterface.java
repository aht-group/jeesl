package org.jeesl.interfaces.rest.system;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.xsd.Container;

public interface JeeslSystemRestInterface
{
//	void x();
	org.jeesl.model.xml.xsd.Container exportStatus(String code) throws UtilsConfigurationException;
	org.jeesl.model.xml.xsd.Container updateTranslation(String code, Container xml) throws UtilsConfigurationException;
	Entity exportRevisionEntity(String code) throws UtilsConfigurationException;
	
}