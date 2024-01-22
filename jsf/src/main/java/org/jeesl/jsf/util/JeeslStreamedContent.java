package org.jeesl.jsf.util;

import java.io.InputStream;

import org.jeesl.interfaces.controller.report.format.JeeslCsvReport;
import org.jeesl.interfaces.controller.report.format.JeeslDocReport;
import org.jeesl.interfaces.controller.report.format.JeeslPdfReport;
import org.jeesl.interfaces.controller.report.format.JeeslXlsReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.primefaces.model.DefaultStreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslStreamedContent
{
	final static Logger logger = LoggerFactory.getLogger(JeeslStreamedContent.class);
	
	public static <REPORT extends JeeslIoReport<?,?,?,?>> DefaultStreamedContent xlsx(JeeslXlsReport<REPORT> report)
	{
		logger.info("Building XLSX for "+JeeslXlsReport.class.getSimpleName());
		try
		{
			InputStream is = report.xlsStream();
			return DefaultStreamedContent.builder().stream(() -> is).contentType(JeeslXlsReport.mimeType).name(report.xlsFileName()).build();
		}
		catch (Exception e) {e.printStackTrace();}
		return null;
	}
	public static DefaultStreamedContent xlsx(InputStream is, String fileName)
	{
		logger.info("Building XLSX for "+JeeslXlsReport.class.getSimpleName());
		try
		{
			return DefaultStreamedContent.builder().stream(() -> is).contentType(JeeslXlsReport.mimeType).name(fileName).build();
		}
		catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static <REPORT extends JeeslIoReport<?,?,?,?>> DefaultStreamedContent pdf(JeeslPdfReport<REPORT> report)
	{
		logger.info("Building PDF for "+JeeslXlsReport.class.getSimpleName());
		try
		{
			InputStream is = report.pdfStream();
			return DefaultStreamedContent.builder().stream(() -> is).contentType(JeeslPdfReport.mimeType).name(report.pdfFileName()).build();
		}
		catch (Exception e) {e.printStackTrace();}
		return null;
	}
	public static DefaultStreamedContent pdf(InputStream is, String fileName)
	{
		try
		{
			return DefaultStreamedContent.builder().stream(() -> is).contentType(JeeslPdfReport.mimeType).name(fileName).build();
		}
		catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static <REPORT extends JeeslIoReport<?,?,?,?>> DefaultStreamedContent docx(JeeslDocReport report)
	{
		try
		{
			InputStream is = report.docStream();
			return DefaultStreamedContent.builder().stream(() -> is).contentType(JeeslDocReport.mimeType).name(report.docFileName()).build();
		}
		catch (Exception e) {e.printStackTrace();}
		return null;
	}
	public static DefaultStreamedContent docx(InputStream is, String fileName)
	{
		try
		{
			return DefaultStreamedContent.builder().stream(() -> is).contentType(JeeslDocReport.mimeType).name(fileName).build();
		}
		catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static <REPORT extends JeeslIoReport<?,?,?,?>> DefaultStreamedContent csv(JeeslCsvReport<REPORT> report)
	{
		logger.info("Building XLSX for "+JeeslXlsReport.class.getSimpleName());
		try
		{
			InputStream is = report.csvStream();
			return DefaultStreamedContent.builder().stream(() -> is).contentType(JeeslCsvReport.mimeType).name(report.csvFileName()).build();
		}
		catch (Exception e) {e.printStackTrace();}
		return null;
	}
}