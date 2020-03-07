package org.jeesl.interfaces.controller.report;

import java.util.Map;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;

public interface JeeslReportHeader <REPORT extends JeeslIoReport<?,?,?,?>>
			extends JeeslReport<REPORT>
{		
	Map<Long,String> getMapAggregationLabels();
}