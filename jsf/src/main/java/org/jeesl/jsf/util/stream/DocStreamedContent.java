package org.jeesl.jsf.util.stream;

import java.io.InputStream;

import org.jeesl.interfaces.controller.report.JeeslDocReport;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class DocStreamedContent extends DefaultStreamedContent implements StreamedContent 
{
	public DocStreamedContent(JeeslDocReport report) throws Exception
	{
		this(report.docStream(),JeeslDocReport.mimeType,report.docFileName());
	}
	
	public DocStreamedContent(InputStream is, String mimeType, String fileName)
	{
		super(is,mimeType,fileName);
	}
		
	public DocStreamedContent(InputStream is, String fileName)
	{
		this(is,JeeslDocReport.mimeType,fileName);
	}
}