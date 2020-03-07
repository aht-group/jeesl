package org.jeesl.interfaces.controller.report;

import java.util.List;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.model.json.JsonFlatFigure;

public interface JeeslFlatReport <REPORT extends JeeslIoReport<?,?,?,?>>
			extends JeeslReport<REPORT>
{		
	List<JsonFlatFigure> getFlatList();
	void buildFlat();
}