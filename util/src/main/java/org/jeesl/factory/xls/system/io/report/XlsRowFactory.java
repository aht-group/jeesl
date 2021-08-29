package org.jeesl.factory.xls.system.io.report;

import java.util.List;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeesl.factory.builder.system.ReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnGroupFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.setting.JeeslReportSetting;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportRow;
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

import net.sf.ahtutils.xml.finance.Figures;

public class XlsRowFactory <L extends JeeslLang,D extends JeeslDescription,
							CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
							REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
							IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
							WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
							SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
							GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
							COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
							ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
							TEMPLATE extends JeeslReportTemplate<L,D,CELL>,CELL extends JeeslReportCell<L,D,TEMPLATE>,STYLE extends JeeslReportStyle<L,D>,CDT extends JeeslStatus<L,D,CDT>,CW extends JeeslStatus<L,D,CW>,
							RT extends JeeslStatus<L,D,RT>,
							ENTITY extends EjbWithId,
							ATTRIBUTE extends EjbWithId,
							TL extends JeeslTrafficLight<L,D,TLS>,
							TLS extends JeeslTrafficLightScope<L,D,TLS,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XlsRowFactory.class);
		
	private final String localeCode;
//	private final ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,?,ENTITY,ATTRIBUTE,TL,TLS,?,?> fbReport;
	
	private final EjbIoReportColumnGroupFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,?,?> efColumnGroup;
	private final EjbIoReportColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,?,?> efColumn;
	
	private XlsCellFactory<REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xfCell;
	
	public XlsRowFactory(String localeCode, final ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,?,ENTITY,ATTRIBUTE,TL,TLS,?,?> fbReport,
			XlsCellFactory<REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xfCell)
	{
		this.localeCode = localeCode;
//		this.fbReport=fbReport;
		efColumnGroup = fbReport.group();
		efColumn = fbReport.column();
		this.xfCell=xfCell;
	}
	
	public void label(Sheet sheet, MutableInt rowNr, ROW ioRow)
    {
		rowNr.add(ioRow.getOffsetRows());
		MutableInt columnNr = new MutableInt(0);
		Row xlsRow = sheet.createRow(rowNr.intValue());
		xfCell.label(xlsRow, columnNr, ioRow);
		rowNr.add(1);
    }
	
	public void labelValue(Sheet xlsSheet, MutableInt rowNr, ROW ioRow, JXPathContext context)
    {
		rowNr.add(ioRow.getOffsetRows());
		Row xlsRow = xlsSheet.createRow(rowNr.intValue());
		
		MutableInt columnNr = new MutableInt(0);
		xfCell.label(xlsRow, columnNr, ioRow);
		xfCell.value(xlsRow, columnNr, ioRow, context);
		
		rowNr.add(1);
    }
	
	@Deprecated public void header(Sheet sheet, MutableInt rowNr, CellStyle dateHeaderStyle, SHEET ioSheet)
    {
		Map<GROUP,Integer> mapSize = efColumnGroup.toMapVisibleGroupSize(ioSheet);

		Row groupingRow = sheet.createRow(rowNr.intValue());
		int columnNr = 0;
		for(GROUP g : EjbIoReportColumnGroupFactory.toListVisibleGroups(ioSheet))
		{
			Cell cell = groupingRow.createCell(columnNr);
            cell.setCellStyle(dateHeaderStyle);
            cell.setCellValue(g.getName().get(localeCode).getLang());
            if(mapSize.get(g)>1)
            {
            	sheet.addMergedRegion(new CellRangeAddress(rowNr.intValue(), rowNr.intValue(), columnNr, columnNr+mapSize.get(g)-1));
            	columnNr = columnNr + mapSize.get(g);
            }
          else{columnNr++;}
		}
		rowNr.add(1);
		
		Row headerRow = sheet.createRow(rowNr.intValue());
		columnNr = 0;
		for(COLUMN c : efColumn.toListVisibleColumns(ioSheet))
		{
			Cell cell = headerRow.createCell(columnNr);
            cell.setCellStyle(dateHeaderStyle);
            cell.setCellValue(c.getName().get(localeCode).getLang());
            columnNr++;
		}
		rowNr.add(1);
    }
	
	public void header(Sheet sheet, MutableInt rowNr, SHEET ioSheet, Map<GROUP,List<String>> mapDynamicGroups, JXPathContext context)
    {
		MutableInt columnNr = new MutableInt(0);
		Map<GROUP,Integer> mapSize = efColumnGroup.toMapVisibleGroupSize(ioSheet);

		Row groupingRow = sheet.createRow(rowNr.intValue());
		for(GROUP g : EjbIoReportColumnGroupFactory.toListVisibleGroups(ioSheet))
		{
			xfCell.header(g, groupingRow, columnNr);
			int sizeOfColumns = mapSize.get(g);
			if(mapDynamicGroups.containsKey(g)) {sizeOfColumns = sizeOfColumns*mapDynamicGroups.get(g).size();}
            if(sizeOfColumns>1)
            {
            	sheet.addMergedRegion(new CellRangeAddress(rowNr.intValue(), rowNr.intValue(), columnNr.intValue()-1, columnNr.intValue()+sizeOfColumns-2));
            	columnNr.add(sizeOfColumns-1);
            }
		}
		rowNr.add(1);
		
		Row headerRow = sheet.createRow(rowNr.intValue());
		columnNr.setValue(0);
		for(COLUMN c : efColumn.toListVisibleColumns(ioSheet))
		{
			if(mapDynamicGroups.containsKey(c.getGroup()))
			{
				for(String key : mapDynamicGroups.get(c.getGroup()))
				{
					if(c.getQueryHeader().trim().isEmpty()) {xfCell.header(c,headerRow,columnNr);}
					else
					{
						String query = c.getQueryHeader().trim().replaceAll("#", key);
						xfCell.header(c,headerRow,columnNr,query,context);
					}
				}
			}
			else {xfCell.header(c,headerRow,columnNr);}
		}
		rowNr.add(1);
    }
	
	public void headerTree(Sheet sheet, MutableInt rowNr, SHEET ioSheet, Figures treeHeader, JeeslReportSetting.Transformation transformation, Figures transformationHeader)
    {
		logger.info("Tranformation:"+transformation);
		
		MutableInt columnNr = new MutableInt(0);
		Map<GROUP,Integer> mapSize = efColumnGroup.toMapVisibleGroupSize(ioSheet);

		Row groupingRow = sheet.createRow(rowNr.intValue());
		boolean treeLabels = true;
		for(GROUP g : EjbIoReportColumnGroupFactory.toListVisibleGroups(ioSheet))
		{
			xfCell.header(g, groupingRow, columnNr);
			
			int size = mapSize.get(g);
			if(treeLabels)
			{
				size = size+treeHeader.getFigures().size()-1;
				treeLabels = false;
			}
			else if(transformation==JeeslReportSetting.Transformation.last)
			{
				size=transformationHeader.getFigures().size();
			}
			
            if(size>1)
            {
            	sheet.addMergedRegion(new CellRangeAddress(rowNr.intValue(), rowNr.intValue(), columnNr.intValue()-1, columnNr.intValue()+size-2));
            	columnNr.add(size-1);
            }
		}
		rowNr.add(1);
			
		Row headerRow = sheet.createRow(rowNr.intValue());
		columnNr.setValue(0);
		for(COLUMN c : efColumn.toListVisibleColumns(ioSheet))
		{
			if(c.getQueryCell().equals("g1"))
			{
				for(Figures header : treeHeader.getFigures())
				{
					xfCell.header(c,headerRow,columnNr,header.getLabel());
				}
			}
			else
			{
				logger.info("NO HEADER COL");
				switch(transformation)
				{
					case none: xfCell.header(c,headerRow,columnNr);break;
					case last: for(Figures f : transformationHeader.getFigures())
								{
									xfCell.header(c,headerRow,columnNr,f.getLabel());
								}
								break;
				}
				
			}
			
		}
		rowNr.add(1);
    }
	
	public static void header(Sheet sheet, MutableInt rowNr, CellStyle dateHeaderStyle, String[] headers)
    {
		header(sheet, rowNr.intValue(), dateHeaderStyle, headers);
		rowNr.add(1);
    }
	
	public static void header(Sheet sheet, int rowNr, CellStyle dateHeaderStyle, String[] headers)
    {
        Row     headerRow = sheet.createRow(rowNr);
        Integer cellNr = 0;
        for (String header : headers)
        {
			Cell cell = headerRow.createCell(cellNr);
                            cell.setCellStyle(dateHeaderStyle);
                            cell.setCellValue(header);
            cellNr++;
        }
    }
	
	public static Row build(Sheet sheet, MutableInt nr)
	{
		Row row;
		row = sheet.getRow(nr.intValue());
		if(row==null){row = sheet.createRow(nr.intValue());}
		nr.add(1);
		return row;
	}
}