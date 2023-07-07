package org.jeesl.interfaces.model.io.report.xlsx;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslReportWorkbook<REPORT extends JeeslIoReport<?,?,?,?>,
									SHEET extends JeeslReportSheet<?,?,?,?,?,?>>
		extends Serializable,EjbWithId,EjbPersistable,EjbSaveable,EjbRemoveable
{					
	REPORT getReport();
	void setReport(REPORT report);
	
	List<SHEET> getSheets();
	void setSheets(List<SHEET> sheets);
}