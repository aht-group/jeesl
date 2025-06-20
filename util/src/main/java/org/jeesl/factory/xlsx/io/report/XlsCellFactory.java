package org.jeesl.factory.xlsx.io.report;

import java.util.Date;
import java.util.Objects;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.JXPathInvalidSyntaxException;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportLayout;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsCellFactory <
							GROUP extends JeeslReportColumnGroup<?,?,?,COLUMN,STYLE>,
							COLUMN extends JeeslReportColumn<?,?,GROUP,STYLE,CDT,CW,?>,
							ROW extends JeeslReportRow<?,?,?,TEMPLATE,CDT,?>,
							TEMPLATE extends JeeslReportTemplate<?,?,CELL>,
							CELL extends JeeslReportCell<?,?,TEMPLATE>,
							STYLE extends JeeslReportStyle<?,?,ALIGNMENT>,
							ALIGNMENT extends JeeslReportAlignment<?,?,ALIGNMENT,?>,
							CDT extends JeeslReportCellType<?,?,CDT,?>,
							CW extends JeeslStatus<?,?,CW>>
{
	final static Logger logger = LoggerFactory.getLogger(XlsCellFactory.class);
		
	private String localeCode;
	
	private XlsStyleFactory<GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT> xfStyle;
	
	public XlsCellFactory(String localeCode, XlsStyleFactory<GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT> xfStyle)
	{
		this.localeCode = localeCode;
		this.xfStyle=xfStyle;
	}
	
	public void header(GROUP ioGroup, Row xlsRow, MutableInt columnNr)
	{
		boolean showLabel = ioGroup.getShowLabel()!=null && ioGroup.getShowLabel();
		if(showLabel){XlsCellFactory.build(xlsRow,columnNr,xfStyle.get(JeeslReportLayout.Style.header,ioGroup),ioGroup.getName().get(localeCode).getLang());}
		else{XlsCellFactory.build(xlsRow,columnNr,xfStyle.get(JeeslReportLayout.Style.header,ioGroup),null);}
	}
	public void header(COLUMN ioColumn, Row xlsRow, MutableInt columnNr)
	{
		boolean showLabel = ioColumn.getShowLabel()!=null && ioColumn.getShowLabel();
		if(showLabel){XlsCellFactory.build(xlsRow,columnNr,xfStyle.get(JeeslReportLayout.Style.header,ioColumn),ioColumn.getName().get(localeCode).getLang());}
		else{XlsCellFactory.build(xlsRow,columnNr,xfStyle.get(JeeslReportLayout.Style.header,ioColumn),null);}
	}
	public void header(COLUMN ioColumn, Row xlsRow, MutableInt columnNr, String query, JXPathContext context)
	{
		boolean showLabel = ioColumn.getShowLabel()!=null && ioColumn.getShowLabel();
		if(showLabel)
		{
//			logger.info(query);
			JeeslReportLayout.Data dt = xfStyle.getDataType(ioColumn);
			CellStyle style = xfStyle.get(JeeslReportLayout.Style.header,ioColumn);
			try
			{
				add(xlsRow, columnNr, context, query, style, JeeslReportLayout.Data.string);
			}
			catch (JXPathInvalidSyntaxException e)
			{
				logger.error(JXPathInvalidSyntaxException.class.getSimpleName()+" at "+ioColumn.getGroup().getPosition()+" "+ioColumn.getPosition());;
				throw e;
			}
		}
		else{XlsCellFactory.build(xlsRow,columnNr,xfStyle.get(JeeslReportLayout.Style.header,ioColumn),null);}
	}
	
	public void header(COLUMN ioColumn, Row xlsRow, MutableInt columnNr, String label)
	{
		XlsCellFactory.build(xlsRow,columnNr,xfStyle.get(JeeslReportLayout.Style.header,ioColumn),label);
	}
	
	public void cell(COLUMN ioColumn, Row xlsRow, MutableInt columnNr, JXPathContext context)
	{
		JeeslReportLayout.Data dt = xfStyle.getDataType(ioColumn);
		CellStyle style = xfStyle.get(JeeslReportLayout.Style.cell,ioColumn);
		if (ioColumn.getStyleCell()!=null)
		{
			style = xfStyle.get(JeeslReportLayout.Style.cell, ioColumn);
		}
		try {add(xlsRow, columnNr, context, ioColumn.getQueryCell(), style, dt);}
		catch (JXPathInvalidSyntaxException e)
		{
			logger.error(JXPathInvalidSyntaxException.class.getSimpleName()+" at "+ioColumn.getGroup().getPosition()+" "+ioColumn.getPosition());;
			throw e;
		}
	}
	public void cell(COLUMN ioColumn, Row xlsRow, MutableInt columnNr, JXPathContext context, String queryCode)
	{
		String query = ioColumn.getQueryCell().replaceAll("#", queryCode);
		
		JeeslReportLayout.Data dt = xfStyle.getDataType(ioColumn);
		CellStyle style = xfStyle.get(JeeslReportLayout.Style.cell,ioColumn);
		try {this.add(xlsRow, columnNr, context, query, style, dt);}
		catch (JXPathInvalidSyntaxException e)
		{
			logger.error(JXPathInvalidSyntaxException.class.getSimpleName()+" at "+ioColumn.getGroup().getPosition()+" "+ioColumn.getPosition());;
			throw e;
		}
	}
	
	public void cell(COLUMN ioColumn, Row xlsRow, MutableInt columnNr, Object object)
	{
		CellStyle style = xfStyle.get(JeeslReportLayout.Style.cell,ioColumn);
		build(xlsRow, columnNr, style, object);
	}
	
	public void cell(COLUMN ioColumn, Row xlsRow, int columnNr, Object object)
	{
		CellStyle style = xfStyle.get(JeeslReportLayout.Style.cell,ioColumn);
		build(xlsRow, columnNr, style,object);
	}
	
	public void footer(COLUMN ioColumn, Row xlsRow, MutableInt columnNr, JXPathContext context)
	{
		JeeslReportLayout.Data dt = xfStyle.getDataType(ioColumn);
		CellStyle style = xfStyle.get(JeeslReportLayout.Style.footer,ioColumn);
		add(xlsRow, columnNr, context, ioColumn.getQueryFooter(), style, dt);
	}
	
	private void add(Row xlsRow, MutableInt columnNr, JXPathContext context, String query, CellStyle style, JeeslReportLayout.Data dt)
	{
//		logger.info(columnNr.intValue()+" dataType:"+dt+" "+query);
		try
		{
			Object value = context.getValue(query);
//			logger.info(columnNr.intValue()+" "+value);
			if(Objects.nonNull(value))
			{
//				logger.info(dt+" "+value.toString()+" "+value.getClass().getSimpleName());
				try
				{
					switch(dt)
					{
						case string: XlsCellFactory.build(xlsRow,columnNr,style,value.toString());	break;
						case dble: 	XlsCellFactory.build(xlsRow,columnNr,style,(Double)value);	break;
						case intgr:		Integer iValue;
										if(value instanceof String){iValue = Integer.valueOf((String)value);}
										else {iValue = (Integer)value;}
										XlsCellFactory.build(xlsRow,columnNr,style,iValue);	break;
						case lng: XlsCellFactory.build(xlsRow,columnNr,style,(Long)value);	break;
						case dte: XlsCellFactory.build(xlsRow,columnNr,style,(XMLGregorianCalendar)value); break;
						case bool: XlsCellFactory.build(xlsRow,columnNr,style,(Boolean)value); break;
						default: XlsCellFactory.build(xlsRow,columnNr,style,(String)value);
					}
				}
				catch (ClassCastException e)
				{
//					logger.error("Row: "+xlsRow+" Column:"+columnNr);
					e.printStackTrace();
				}
			}
			else
			{
//				logger.warn(dt+" value is null");
				columnNr.add(1);
			}
		}
		catch(JXPathNotFoundException e)
		{
//			e.printStackTrace();
			columnNr.add(1);
		}
		catch(JXPathException e)
		{
//			e.printStackTrace();
			columnNr.add(1);
		}
	}
	
	public void label(Row xlsRow, MutableInt columnNr, ROW ioRow)
	{
		XlsCellFactory.build(xlsRow,columnNr,xfStyle.getStyleLabelLeft(),ioRow.getName().get(localeCode).getLang());
	}
	
	public void value(Row xlsRow, MutableInt columnNr, ROW ioRow, JXPathContext context)
	{
		try
		{
			Object value = context.getValue(ioRow.getQueryCell());
			if(value==null){columnNr.add(1);}
			else
			{
				CellStyle style = xfStyle.get(ioRow);
				switch(xfStyle.getDataType(ioRow))
				{
					case string: XlsCellFactory.build(xlsRow,columnNr,style,(String)value);	break;
					case dble: 	XlsCellFactory.build(xlsRow,columnNr,style,(Double)value);	break;
					case intgr:		Integer iValue;
									if(value instanceof String){iValue = Integer.valueOf((String)value);}
									else {iValue = (Integer)value;}
									XlsCellFactory.build(xlsRow,columnNr,style,iValue);	break;
					case lng: XlsCellFactory.build(xlsRow,columnNr,style,(Long)value);	break;
					case dte: XlsCellFactory.build(xlsRow,columnNr,style,(XMLGregorianCalendar)value); break;
					case bool: XlsCellFactory.build(xlsRow,columnNr,style,(Boolean)value); break;
					default: XlsCellFactory.build(xlsRow,columnNr,style,(String)value);
				}
			}
		}
		catch (JXPathNotFoundException e){columnNr.add(1);}
	}
	
	public void build(Sheet xslSheet, MutableInt rowNr, ROW ioRow)
	{
		int rootRow = rowNr.intValue();
		int rootColumn = ioRow.getOffsetColumns();
		int maxRow = rootRow;
		
		for(CELL ioCell : ioRow.getTemplate().getCells())
		{
			int cellRow = rootRow+ioCell.getRowNr()-1;
			int cellCol = rootColumn+ioCell.getColNr()-1;
			if(cellRow>maxRow){maxRow=cellRow;}
			
			Row xlsRow = xslSheet.getRow(cellRow); if(xlsRow==null){xlsRow = xslSheet.createRow(cellRow);}
			
			Cell xlsCell = xlsRow.createCell(cellCol);
			xlsCell.setCellStyle(xfStyle.getStyleFallback());
			xlsCell.setCellValue(ioCell.getName().get(localeCode).getLang());
		}
		rowNr.add(maxRow-rootRow+1);
	}
	
	public static void build(Row xlsRow, MutableInt columnNr, CellStyle style, Object value)
	{
		build(xlsRow,columnNr.intValue(),style,value);
		columnNr.add(1);
	}
	
	public static void build(Row xlsRow, MutableInt columnNr, CellStyle style, Object value, int width)
	{
		build(xlsRow,columnNr.intValue(),style,value, width);
		//logger.info("Writing " +value.toString() +" to " +xlsRow.getRowNum() +", " +columnNr.toString());
		columnNr.add(width);
	}
	
	public static void build(Row xlsRow, int columnNr, CellStyle style, Object value, int width)
	{
		Cell cell = xlsRow.createCell(columnNr);
        cell.setCellStyle(style);
        
		// Merge cells if necessary
		if (width>1)
		{
			xlsRow.getSheet().addMergedRegion(new CellRangeAddress(
			xlsRow.getRowNum(), //first row (0-based)
			xlsRow.getRowNum(), //last row  (0-based)
			columnNr, 			//first column (0-based)
			columnNr+width-1));  //last column  (0-based)
		}
		
	//	logger.info(value.getClass().getSimpleName()+" "+value.toString()+" style:"+style.toString());
        if(value!=null)
        {
	        if(value instanceof String){cell.setCellValue((String)value);}
	        else if(value instanceof Double){cell.setCellValue((Double)value);}
	        else if(value instanceof Long){cell.setCellValue((Long)value);}
	        else if(value instanceof Integer){cell.setCellValue((Integer)value);}
	        else if(value instanceof Date){cell.setCellValue((Date)value);}
	        else if(value instanceof Boolean){cell.setCellValue((Boolean)value);}
	        else if(value instanceof XMLGregorianCalendar){cell.setCellValue(((XMLGregorianCalendar)value).toGregorianCalendar().getTime());}
	        else {cell.setCellValue((String)value);}
        }
	}
	
	public static void build(Row xlsRow, int columnNr, CellStyle style, Object value)
	{
		build(xlsRow, columnNr, style, value, 1);
	}
	 
	public static String debugPosition(int row, int col)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("row:"+(row+1));
		sb.append(" col:"+XlsColumnFactory.index2code(col));
		return sb.toString();
	}
	
	public static void copy(Cell cellIn, Cell cellOut)
	{
		 cellOut.setCellValue(cellIn.getStringCellValue());
	}
}