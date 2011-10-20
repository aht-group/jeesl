package net.sf.ahtutils.report;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import javax.imageio.ImageIO;

import net.sf.ahtutils.report.AbstractReportControl.Direction;
import net.sf.ahtutils.report.AbstractReportControl.Output;
import net.sf.ahtutils.report.exception.ReportException;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.report.Resource;
import net.sf.ahtutils.xml.report.Resources;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

public class ReportController extends AbstractReportControl
{
	static Log logger = LogFactory.getLog(ReportController.class);
	protected Resources resources;
	protected String path, name;
	
	public ReportController(Reports config, Resources resources)
	{
		super(config);
		this.resources = resources;
	}
	
	public void addParameter(String key, Object value)
	{
		mapReportParameter.put(key, value);
	}
	
	public void generateReport(String reportId, Output output, Direction dir, Document data)
	{
		rId = reportId;
		Document doc=data;
		logger.debug("Starting Report Generation");
		compileSubReports(output,dir);
		fillParameterMap(doc);
	}
	
	public void createJasperPrint(String reportId, Output output, Direction dir, Document data)
	{
		rId = reportId;
		Document doc=data;
		this.output = output;
		direction = dir;
		JasperReport report = getMasterReport(output,dir);
		fillParameterMap(doc);
		try {jPrint  = JasperFillManager.fillReport(report, mapReportParameter);}
		catch (JRException e) {e.printStackTrace();}
	}
		
	public void fillParameterMap(Document doc)
	{	
		mapReportParameter.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, doc);
		mapReportParameter.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
		mapReportParameter.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.00");
		mapReportParameter.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
		Locale locale = new Locale("de", "DE");
		mapReportParameter.put(JRParameter.REPORT_LOCALE, locale);
		mapReportParameter.put("REPORT_LOCALE", locale);
		
		for (Resource res : resources.getResource())
		{
			logger.info("Adding resource of type " +res.getType() +" with id='" +res.getName() +"' loaded from " +res.getValue().getValue());
			if (res.getType().equals("image"))
				
				{
					BufferedImage image = null;
					try {
						String imgLocation = reportRoot +"/resources/" +res.getType() +"/" +res.getValue().getValue();
						logger.info("Including image resource: " +imgLocation);
						image = ImageIO.read(mrl.searchIs(imgLocation));} 
					catch (FileNotFoundException e) {logger.error(e.getMessage());}
					catch (IOException e) {logger.error(e.getMessage());}
					mapReportParameter.put(res.getName(), image);
				}
		}
	}
	
	public void setExportParameter(String name)
	{
		setExportParameter("src/test/resources/data/reports/", name);
	}
	
	public void setExportParameter(String path, String name)
	{
		this.path = path;
		this.name = name;
	}

	
	@SuppressWarnings("deprecation")
	public void exportPdf() throws ReportException
	{
		if (!output.equals(Output.pdf))
		{
			throw new ReportException("Wrong format (" +output.toString() +") for exporting to PDF document! Exprected PDF.");
		}
		File f = new File(path, name+".pdf");
		try
		{	
			logger.info("Exporting report to PDF ("+f.getAbsolutePath()+")");
			JasperExportManager.exportReportToPdfFile(jPrint, f.getAbsolutePath());
		}
		catch (JRException e) {logger.error("Error in JasperReport creation: " +e.getMessage());}
	}
	
	@SuppressWarnings("deprecation")
	public void exportXls() throws ReportException
	{
		if (!output.equals(Output.xls))
		{
			throw new ReportException("Wrong format (" +output.toString() +") for exporting to excel sheet! Exprected XLS.");
		}
		File f = new File(path +name+".xls");
		try
		{	
			logger.info("Exporting report to Excel Sheet");
			OutputStream os = new ByteArrayOutputStream();
			JRXlsExporter exporterXLS = new JRXlsExporter();
			exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jPrint);
			exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, os);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporterXLS.exportReport();	
			try
			{
				OutputStream osProcessed = ReportUtilXls.RemoveEmptyCells(os);
				
				OutputStream outputStreamProcessed = new FileOutputStream (f); 
				((ByteArrayOutputStream)osProcessed).writeTo(outputStreamProcessed);
			}
			catch (FileNotFoundException e) {logger.error(e);}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		catch (JRException e) {logger.error("Error in JasperReport creation: " +e.getMessage());}
	}
}