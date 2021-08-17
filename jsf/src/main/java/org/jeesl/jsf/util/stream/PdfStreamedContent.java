package org.jeesl.jsf.util.stream;

import java.io.InputStream;

import org.jeesl.interfaces.controller.report.JeeslPdfReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class PdfStreamedContent <REPORT extends JeeslIoReport<?,?,?,?>>
								extends DefaultStreamedContent implements StreamedContent 
{
	public PdfStreamedContent(JeeslPdfReport<REPORT> report) throws Exception
	{
		this(report.pdfStream(),report.pdfFileName());
	}
	
	public PdfStreamedContent(InputStream is, String fileName)
	{
		super(is,JeeslPdfReport.mimeType,fileName);
	}
	
	@Deprecated //public use is deprecated ... use (is,fileName)
	public PdfStreamedContent(InputStream is, String mimeType, String fileName)
	{
		super(is,mimeType,fileName);
	}
}