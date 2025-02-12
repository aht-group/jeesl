package org.jeesl.api.rest.i.io.report;

import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.jeesl.model.xml.io.report.Styles;
import org.jeesl.model.xml.io.report.Templates;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.xsd.Container;

public interface JeeslIoReportImportRestInterface
{
	DataUpdate importSystemIoReportCategories(Container container);
	DataUpdate importSystemIoReports(Reports reports);
	DataUpdate importSystemIoReport(Report report);
	DataUpdate importSystemIoReportTemplates(Templates templates);
	DataUpdate importSystemIoReportStyles(Styles styles);
}