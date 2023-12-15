package org.jeesl.api.rest.rs.io.report;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.interfaces.util.qualifier.JeeslRestSecured;
import org.jeesl.model.xml.xsd.Container;

import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.report.Styles;
import net.sf.ahtutils.xml.report.Templates;

public interface JeeslIoReportRestExport
{
	@JeeslRestSecured
	@GET @Path("/system/io/report/category") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoReportCategories();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/styles") @Produces(MediaType.APPLICATION_XML)
	Styles exportSystemIoReportStyles();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/aggregation") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoReportAggregation();
	
	@JeeslRestSecured
	@GET @Path("/system/io/reports") @Produces(MediaType.APPLICATION_XML)
	Reports exportSystemIoReports();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/{code}") @Produces(MediaType.APPLICATION_XML)
	Reports exportSystemIoReport(@PathParam("code") String code);
	
	@GET @Path("/system/io/report/templates") @Produces(MediaType.APPLICATION_XML)
	Templates exportSystemIoReportTemplates();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/style/alignment") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoReportStyleAlignment();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/setting/filling") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoReportSettingFilling();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/setting/transformation") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoReportSettingTransformation();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/setting/implementation") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoReportSettingImplementation();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/row/type") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoReportRowType();
	
	@JeeslRestSecured
	@GET @Path("/system/io/report/colum/width") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoReportColumnWidth();
}