package org.jeesl.factory.xml.system.io.report;

import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.lang3.ObjectUtils;
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
import org.jeesl.model.xml.io.report.Row;
import org.jeesl.model.xml.io.report.Rows;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportRowComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlRowsFactory <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,?,WORKBOOK,GROUP,ROW>,
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
	final static Logger logger = LoggerFactory.getLogger(XmlRowsFactory.class);
	
	private Rows q;
	
	private Comparator<ROW> cRow;
	
	private XmlRowFactory<L,D,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT> xfRow;
	
	public XmlRowsFactory(String localeCode, Rows q)
	{
		this.q=q;
		cRow = new IoReportRowComparator<L,D,CATEGORY,REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW>().factory(IoReportRowComparator.Type.position);
		if(ObjectUtils.isNotEmpty(q.getRow())) {xfRow = new XmlRowFactory<>(localeCode,q.getRow().get(0));}
	}
	
	public Rows build(SHEET sheet)
	{
		Rows xml = new Rows();
				
		if(ObjectUtils.isNotEmpty(q.getRow()))
		{
			Collections.sort(sheet.getRows(),cRow);
			for(ROW row : sheet.getRows())
			{
				xml.getRow().add(xfRow.build(row));
			}
		}
		
		return xml;
	}
	
	public static Rows build(Row row)
	{
		Rows xml = new Rows();		
		xml.getRow().add(row);
		return xml;
	}
}