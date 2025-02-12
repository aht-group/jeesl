package org.jeesl.api.rest.i.io.report;

import javax.ws.rs.PathParam;

import org.jeesl.model.xml.io.report.Reports;
import org.jeesl.model.xml.io.report.Styles;
import org.jeesl.model.xml.io.report.Templates;
import org.jeesl.model.xml.xsd.Container;

public interface JeeslIoReportExportRestInterface
{

	Container exportSystemIoReportCategories();

	Styles exportSystemIoReportStyles();

	Reports exportSystemIoReports();

	Reports exportSystemIoReport(@PathParam("code") String code);

	Templates exportSystemIoReportTemplates();
}