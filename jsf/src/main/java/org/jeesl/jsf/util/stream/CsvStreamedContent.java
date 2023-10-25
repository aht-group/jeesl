package org.jeesl.jsf.util.stream;

import org.jeesl.interfaces.controller.report.format.JeeslCsvReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class CsvStreamedContent <REPORT extends JeeslIoReport<?,?,?,?>>
								extends DefaultStreamedContent implements StreamedContent 
{
	private static final long serialVersionUID = 1L;

	public CsvStreamedContent(JeeslCsvReport<REPORT> report) throws Exception
	{
		super(report.csvStream(),JeeslCsvReport.mimeType,report.csvFileName());
	}

//	public CsvStreamedContent(InputStream is, String fileName)
//	{
//		super(is,JeeslXlsReport.mimeType,fileName);
//	}
}