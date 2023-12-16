package org.jeesl.api.facade.system;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.xsd.Container;

import net.sf.ahtutils.xml.report.Reports;

public interface JeeslExportRestFacade
{	
	public final static String urlJeesl = "https://www.jeesl.org/jeesl";
	public final static String urlGeojsf = "https://www.geojsf.org/geojsf";
	
	public final static String packageJeesl = "org.jeesl";
	public final static String packageGeojsf = "org.geojsf";
	
	Container exportJeeslReferenceRest(String code) throws UtilsConfigurationException;
	Entity exportJeeslReferenceRevisionEntity(String code) throws UtilsConfigurationException;
	Reports exportIoReport(String code);
}