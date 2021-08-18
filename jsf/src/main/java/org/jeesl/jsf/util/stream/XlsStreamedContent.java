package org.jeesl.jsf.util.stream;

import java.io.InputStream;

import org.jeesl.interfaces.controller.report.JeeslXlsReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class XlsStreamedContent <REPORT extends JeeslIoReport<?,?,?,?>>
								extends DefaultStreamedContent implements StreamedContent 
{
	public XlsStreamedContent(JeeslXlsReport<REPORT> report) throws Exception
	{
		super(report.xlsStream(),JeeslXlsReport.mimeType,report.xlsFileName());
	}

	public XlsStreamedContent(InputStream is, String fileName)
	{
		super(is,JeeslXlsReport.mimeType,fileName);
	}
}