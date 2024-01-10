package org.jeesl.jsf.util;

import java.io.InputStream;

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
}