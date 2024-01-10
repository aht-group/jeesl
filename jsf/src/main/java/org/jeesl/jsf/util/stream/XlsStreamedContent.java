package org.jeesl.jsf.util.stream;

import java.io.InputStream;

import org.jeesl.interfaces.controller.report.format.JeeslXlsReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsStreamedContent <REPORT extends JeeslIoReport<?,?,?,?>>
								extends DefaultStreamedContent implements StreamedContent 
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(XlsStreamedContent.class);
	
	public XlsStreamedContent(JeeslXlsReport<REPORT> report) throws Exception
	{
		super(report.xlsStream(),JeeslXlsReport.mimeType,report.xlsFileName());
		logger.warn("Building .. deprecated");
//		DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(bytes)).contentType("application/pdf").name(frh.getMeta().getFileName()).build();
	}

	public XlsStreamedContent(InputStream is, String fileName)
	{
		super(is,JeeslXlsReport.mimeType,fileName);
	}
}