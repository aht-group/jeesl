package org.jeesl.interfaces.controller.report.format;

import java.io.InputStream;

import org.jeesl.interfaces.controller.report.JeeslReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;

public interface JeeslXlsReport <REPORT extends JeeslIoReport<?,?,?,?>> extends JeeslReport<REPORT>
{		
	public static String mimeType = "application/msexcel";
	
	public InputStream xlsStream() throws Exception;
	public String xlsFileName();
}