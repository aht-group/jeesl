package org.jeesl.api.facade.io;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslIoReportFacade <
										REPORT extends JeeslIoReport<?,?,?,WORKBOOK>,
										IMPLEMENTATION extends JeeslStatus<?,?,IMPLEMENTATION>,
										WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
										SHEET extends JeeslReportSheet<?,?,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
										GROUP extends JeeslReportColumnGroup<?,?,SHEET,COLUMN,?>,
										COLUMN extends JeeslReportColumn<?,?,GROUP,?,?,?,?>,
										ROW extends JeeslReportRow<?,?,SHEET,TEMPLATE,?,?>,
										TEMPLATE extends JeeslReportTemplate<?,?,CELL>,
										CELL extends JeeslReportCell<?,?,TEMPLATE>>
			extends JeeslFacade
{	
	REPORT load(REPORT report, boolean recursive);
	WORKBOOK load(WORKBOOK workbook);
	SHEET load(SHEET sheet, boolean recursive);
	GROUP load(GROUP group);
	TEMPLATE load(TEMPLATE template);
	
	SHEET fSheet(WORKBOOK workbook, String code) throws JeeslNotFoundException;
	
	void rmSheet(SHEET sheet) throws JeeslConstraintViolationException;
	void rmGroup(GROUP group) throws JeeslConstraintViolationException;
	void rmColumn(COLUMN column) throws JeeslConstraintViolationException;
	void rmRow(ROW row) throws JeeslConstraintViolationException;
	void rmCell(CELL cell) throws JeeslConstraintViolationException;
}