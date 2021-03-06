package org.jeesl.interfaces.controller.report;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;

public interface JeeslZipReport <REPORT extends JeeslIoReport<?,?,?,?>>
			extends JeeslReport<REPORT>
{	
	public static String mimeType = "application/zip";
}