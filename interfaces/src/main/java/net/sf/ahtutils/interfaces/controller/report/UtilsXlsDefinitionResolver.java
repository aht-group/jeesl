package net.sf.ahtutils.interfaces.controller.report;

import org.jeesl.model.xml.io.report.XlsWorkbook;

public interface UtilsXlsDefinitionResolver
{		
	public XlsWorkbook definition(String code);
}