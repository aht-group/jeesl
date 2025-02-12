package org.jeesl.api.rest.rs.io.report;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.io.report.JeeslIoReportExportRestInterface;
import org.jeesl.interfaces.util.qualifier.JeeslRestSecured;
import org.jeesl.model.xml.io.report.Reports;
import org.jeesl.model.xml.io.report.Styles;
import org.jeesl.model.xml.io.report.Templates;
import org.jeesl.model.xml.xsd.Container;

@Path("/db/export")
public interface JeeslIoReportExportRest extends JeeslIoReportExportRestInterface
{
	@JeeslRestSecured
	@GET @Path("/system/io/report/category") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoReportCategories();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/styles") @Produces(MediaType.APPLICATION_XML)
	Styles exportSystemIoReportStyles();

	@JeeslRestSecured
	@GET @Path("/system/io/reports") @Produces(MediaType.APPLICATION_XML)
	Reports exportSystemIoReports();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/{code}") @Produces(MediaType.APPLICATION_XML)
	Reports exportSystemIoReport(@PathParam("code") String code);
	
	@GET @Path("/system/io/report/templates") @Produces(MediaType.APPLICATION_XML)
	Templates exportSystemIoReportTemplates();
}