package org.jeesl.api.rest.rs.io.report;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.interfaces.util.qualifier.JeeslRestSecured;
import org.jeesl.model.xml.xsd.Container;

import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.report.Styles;
import net.sf.ahtutils.xml.report.Templates;
import net.sf.ahtutils.xml.sync.DataUpdate;

public interface JeeslIoReportRestImport
{
	@JeeslRestSecured
	@POST @Path("/system/io/report/category") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportCategories(Container container);
	
	@JeeslRestSecured
	@POST @Path("/system/io/report/aggregation") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportAggregation(Container container);
	
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
	
	@JeeslRestSecured
	@POST @Path("/system/io/report/style/alignment") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportStyleAlignment(Container container);
	
	@JeeslRestSecured
	@POST @Path("/system/io/report/setting/filling") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportSettingFilling(Container container);
	
	@JeeslRestSecured
	@POST @Path("/system/io/report/setting/transformation") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportSettingTransformation(Container container);
	
	@JeeslRestSecured
	@POST @Path("/system/io/report/setting/implementation") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportSettingImplementation(Container container);
	
	@JeeslRestSecured
	@POST @Path("/system/io/report/row/type") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportRowType(Container container);
	
	@JeeslRestSecured
	@POST @Path("/system/io/report/colum/width") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoReportColumnWidth(Container container);
}