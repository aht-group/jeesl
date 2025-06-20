package org.jeesl.factory.xml.system.io.report;

import java.util.Objects;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportColumnWidth;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.XlsWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlWorkbookFactory <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
								ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslReportColumnWidth<L,D,CW,?>,
								RT extends JeeslReportRowType<L,D,RT,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlWorkbookFactory.class);
	
	private XlsWorkbook q;
	
	private XmlSheetsFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT> xfSheets;
	
	public XmlWorkbookFactory(String localeCode, XlsWorkbook q)
	{
		this.q=q;
		if(Objects.nonNull(q.getXlsSheets())) {xfSheets = new XmlSheetsFactory<>(localeCode,q.getXlsSheets());}
	}
	
	public XlsWorkbook build(WORKBOOK workbook)
	{
		XlsWorkbook xml = new XlsWorkbook();
		
		if(Objects.nonNull(q.getXlsSheets())) {xml.setXlsSheets(xfSheets.build(workbook));}
						
		return xml;
	}
}