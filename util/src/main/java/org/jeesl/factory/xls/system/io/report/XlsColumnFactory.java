package org.jeesl.factory.xls.system.io.report;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportLayout;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsColumnFactory <L extends JeeslLang,D extends JeeslDescription,
							CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
							REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
							IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
							WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
							SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
							GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
							COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
							ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
							TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
							CELL extends JeeslReportCell<L,D,TEMPLATE>,
							STYLE extends JeeslReportStyle<L,D>,
							CDT extends JeeslStatus<L,D,CDT>,CW extends JeeslStatus<L,D,CW>,
							RT extends JeeslReportRowType<L,D,RT,?>,
							ENTITY extends EjbWithId,
							ATTRIBUTE extends EjbWithId,
							TL extends JeeslTrafficLight<L,D,TLS>,
							TLS extends JeeslTrafficLightScope<L,D,TLS,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XlsColumnFactory.class);
		
	public XlsColumnFactory()
	{
		
	}
	
	public void trackWidth(Sheet sheet, List<COLUMN> columns)
	{
		if(sheet instanceof SXSSFSheet)
		{
			for(int i=0; i<columns.size(); i++)
	        {
				COLUMN ioColumn = columns.get(i);
				if(ioColumn.getColumWidth()!=null)
				{
					switch(JeeslReportLayout.ColumnWidth.valueOf(ioColumn.getColumWidth().getCode()))
					{
						case none: break;
						case auto: ((SXSSFSheet)sheet).trackColumnForAutoSizing(i);break;
						case min: break;
						default: break;
					}
				}
	        }
		}
	}
	
	public void adjustWidth(Sheet sheet, List<COLUMN> columns)
    {
		for(int i=0; i<columns.size(); i++)
        {
			COLUMN ioColumn = columns.get(i);
			if(ioColumn.getColumWidth()!=null)
			{
				switch(JeeslReportLayout.ColumnWidth.valueOf(ioColumn.getColumWidth().getCode()))
				{
					case none: break;
					case auto: sheet.autoSizeColumn(i);break;
					case min: sheet.setColumnWidth(i, ioColumn.getColumSize());break;
					default: break;
				}
			}
        }
    }
	
	public static int code2index(String code)
	{
		int result = 0;
		for(int i=0;i<code.length();i++)
		{
			if(Character.isUpperCase(code.charAt(i))){result = result *26 +(code.charAt(i)-('A'-1));}			
			else{result = result *26 +(code.charAt(i)-('a'-1));}
		}
		return result -1;
	}
	
	public static String index2code(int index)
	{
        return CellReference.convertNumToColString(index);
	}
}
