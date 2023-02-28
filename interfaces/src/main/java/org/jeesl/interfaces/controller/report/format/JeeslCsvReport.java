package org.jeesl.interfaces.controller.report.format;

import java.io.InputStream;

import org.jeesl.interfaces.controller.report.JeeslReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;

public interface JeeslCsvReport <REPORT extends JeeslIoReport<?,?,?,?>> extends JeeslReport<REPORT>
{		
	public static String mimeType = "application/msexcel";
	
	public InputStream csvStream() throws Exception;
	public String csvFileName();
}