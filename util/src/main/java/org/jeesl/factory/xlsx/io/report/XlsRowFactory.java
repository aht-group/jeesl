package org.jeesl.factory.xlsx.io.report;

import java.util.List;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeesl.factory.builder.io.IoReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnGroupFactory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.setting.JeeslReportSetting;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportColumnWidth;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.module.finance.Figures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsRowFactory <
							WORKBOOK extends JeeslReportWorkbook<?,SHEET>,
							SHEET extends JeeslReportSheet<?,?,?,WORKBOOK,GROUP,ROW>,
							GROUP extends JeeslReportColumnGroup<?,?,SHEET,COLUMN,STYLE>,
							COLUMN extends JeeslReportColumn<?,?,GROUP,STYLE,CDT,CW,?>,
							ROW extends JeeslReportRow<?,?,SHEET,TEMPLATE,CDT,RT>,
							TEMPLATE extends JeeslReportTemplate<?,?,CELL>,
							CELL extends JeeslReportCell<?,?,TEMPLATE>,
							STYLE extends JeeslReportStyle<?,?,ALIGNMENT>,
							ALIGNMENT extends JeeslReportAlignment<?,?,ALIGNMENT,?>,
							CDT extends JeeslReportCellType<?,?,CDT,?>,
							CW extends JeeslReportColumnWidth<?,?,CW,?>,
							RT extends JeeslReportRowType<?,?,RT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XlsRowFactory.class);
		
	private final String localeCode;
//	private final ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,?,ENTITY,ATTRIBUTE,TL,TLS,?,?> fbReport;
	
	private final EjbIoReportColumnGroupFactory<?,?,SHEET,GROUP,COLUMN,STYLE> efColumnGroup;
	private final EjbIoReportColumnFactory<?,?,SHEET,GROUP,COLUMN,ROW,CELL,STYLE,CDT,CW,RT> efColumn;
	
	private XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xfCell;
	
	public XlsRowFactory(String localeCode, final IoReportFactoryBuilder<?,?,?,?,?,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,?,?,?,?,?,?,?> fbReport,
			XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xfCell)
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