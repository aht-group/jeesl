package org.jeesl.factory.txt.system.io.mail.core;

import javax.activation.MimetypesFileTypeMap;

import org.jeesl.interfaces.controller.report.format.JeeslPdfReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtMimeTypeFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtMimeTypeFactory.class);
	
	private static MimetypesFileTypeMap mftm = new MimetypesFileTypeMap();
	
	public static String build(String fileName)
	{
		if(fileName.endsWith(".pdf")){return JeeslPdfReport.mimeType;}
		
		
		return mftm.getContentType(fileName);
	}
}