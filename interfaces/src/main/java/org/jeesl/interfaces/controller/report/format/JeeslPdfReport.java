package org.jeesl.interfaces.controller.report.format;

import java.io.InputStream;

import org.jeesl.interfaces.controller.report.JeeslReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;

public interface JeeslPdfReport <REPORT extends JeeslIoReport<?,?,?,?>>
			extends JeeslReport<REPORT>
{	
	public static String mimeType = "application/pdf";
	
	public InputStream pdfStream() throws Exception;
	public String pdfFileName();
}