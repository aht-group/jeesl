package org.jeesl.interfaces.controller.report.format;

import java.util.List;

import org.jeesl.interfaces.controller.report.JeeslReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;

public interface JeeslJsonReport <REPORT extends JeeslIoReport<?,?,?,?>>
			extends JeeslReport<REPORT>
{		
	public String getJsonStream() throws Exception;
	public void buildJson();
	public List<Object> getJsonDataList();
}