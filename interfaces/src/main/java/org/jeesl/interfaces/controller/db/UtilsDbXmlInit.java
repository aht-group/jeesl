package org.jeesl.interfaces.controller.db;

import java.io.FileNotFoundException;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.processing.UtilsConfigurationException;

public interface UtilsDbXmlInit
{
	public static enum Priority{statics,required,mandatory,optional,A,B,C,D,E}
	
	void initFromXml(Priority priority) throws FileNotFoundException,JeeslConstraintViolationException,UtilsConfigurationException;
}
