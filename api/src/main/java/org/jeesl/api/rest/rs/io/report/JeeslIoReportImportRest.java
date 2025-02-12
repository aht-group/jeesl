package org.jeesl.api.rest.rs.io.report;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.interfaces.util.qualifier.JeeslRestSecured;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.jeesl.model.xml.io.report.Styles;
import org.jeesl.model.xml.io.report.Templates;
import org.jeesl.model.xml.xsd.Container;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

@Path("/db/import")
public interface JeeslIoReportImportRest
{
	@JeeslRestSecured
	@POST @Path("/system/io/report/category") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportCategories(Container container);

	@JeeslRestSecured
	@POST @Path("/system/io/reports") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReports(Reports reports);
	
	@JeeslRestSecured
	@POST @Path("/system/io/report") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReport(Report report);
	
	@JeeslRestSecured
	@POST @Path("/system/io/report/templates") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportTemplates(Templates templates);
	
	@JeeslRestSecured
	@POST @Path("/system/io/report/styles") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportStyles(Styles styles);
}