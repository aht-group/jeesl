package net.sf.ahtutils.report.excel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.ahtutils.xml.report.Info;
import net.sf.ahtutils.xml.report.Label;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeesl.model.xml.module.finance.Figures;
import org.jeesl.model.xml.module.finance.Finance;
import org.jeesl.model.xml.module.finance.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractExcelAggregationsFlatExporter
{
	
    // Excel related objects
    public Workbook         wb;
    public Font             headerFont;
    public CellStyle        dateHeaderStyle;
    public CellStyle        numberStyle; 
    public CreationHelper   createHelper;
    public JXPathContext	context;
	
    // The offset where the financial info goes
    private short financeOffset  = 0;

    // The current row number
    private short rowNr          = 0;

    // The number of financial fields
    private short dataFieldCounter = 0;

    // The sheet in the workbook
    private final Sheet sheet;
    
    // The data
    public Object    report;
    
    // The query which entities are the subject of this report
    public String    query;
	
    private final int MIN_WIDTH = 5000;
        
    final static Logger logger = LoggerFactory.getLogger(AbstractExcelAggregationsFlatExporter.class);
	
    public AbstractExcelAggregationsFlatExporter(Info infoSection, Figures firstFigures) throws Exception
    {
        // Create the Excel Workbook and select the created sheet
        wb = new XSSFWorkbook();
        wb.createSheet("Aggregations");
        sheet = wb.getSheet("Aggregations");
        createHelper = wb.getCreationHelper();

        // Create fonts and alter it.
        Font font = wb.createFont();
        font.setBold(true);
		
        Font fontTitle = wb.createFont();
        fontTitle.setItalic(true);

        // Create styles
        dateHeaderStyle = wb.createCellStyle();
        dateHeaderStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy.MM"));
        dateHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dateHeaderStyle.setFont(font);
        dateHeaderStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        dateHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        dateHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        numberStyle = wb.createCellStyle();
        numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
		
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFont(fontTitle);
		
		
        // Create the column headers
        short column        = 0;
        Row row     = sheet.createRow(rowNr);

        List<String> labels = new ArrayList<String>();	
        for (Label label : infoSection.getLabels().getLabel())
        {
            Cell cell   = row.createCell(column);

            cell.setCellStyle(titleStyle);

            if (label.getKey().startsWith("labelLevel"))
            {
                cell.setCellValue(label.getValue());
                column++;
            }
            else if (label.getKey().startsWith("financeLevel"))
            {
                if (financeOffset == 0) {financeOffset = column;}
                labels.add(label.getValue());
                dataFieldCounter++;
                column++;
            }
        }
        column = financeOffset;
        for (int l = labels.size()-1 ; l > -1; l--)
        {
        //  logger.info("Setting " +l + " to " +labels.get(l));
            Cell cell   = row.createCell(column);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(labels.get(l));
            column++;
        }



        logger.debug("Wrote " +(column+1) +" headers.");
        logger.debug("Figures begin at column " +financeOffset +" and contain data for " +dataFieldCounter +" sections.");

        // Proceding with aggregated content
        rowNr = 1;
        for (Figures figures : firstFigures.getFigures())
        {
            short offset = 0;
            addContent(new ArrayList<String>(), figures);
        }

        // Set all columns to a comfortable width of MIN_WIDTH which is 5000 in this case
        for (int i = 0; i<financeOffset+dataFieldCounter; i++)
        {
            sheet.setColumnWidth(i, MIN_WIDTH);
        }

        // Set the borders
        Iterator rIterator = sheet.rowIterator();
        while (rIterator.hasNext())
        {
            Row r = (Row) rIterator.next();
            Cell financeBegin = r.getCell(financeOffset);
            CellStyle borderStyle = wb.createCellStyle();
            CellStyle oldStyle    = financeBegin.getCellStyle();
            borderStyle.cloneStyleFrom(oldStyle);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            financeBegin.setCellStyle(borderStyle);
        }
    }
	
	public void addContent(List<String> parents, Figures figures) throws Exception
	{
            if (parents.size()==(financeOffset-1))
            {
                // Create a new Row
		Row row = sheet.createRow(rowNr);
		
		// Fill in the name of the aggregation entity
                short columnNumber = 0;
                for (String parentLabel : parents)
                {
                    Cell cell = row.createCell(columnNumber);
                    cell.setCellValue(parentLabel);
                    columnNumber++;
                }
                Cell cell = row.createCell(columnNumber);
		cell.setCellValue(figures.getLabel());
                
                // Fill in the financial data (reverse)
		short financeColumn = (short)(financeOffset + dataFieldCounter);
		short position = (short)(financeOffset);
		
		// Prepare Styles
		CellStyle dateStyle = wb.createCellStyle();
		CellStyle numericStyle = wb.createCellStyle();
		
		numericStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
		dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
		
		for (short i = dataFieldCounter; i>0; i --)
		{
                    if (logger.isTraceEnabled()) {logger.trace("Content adding iterator = " +i +" while position = " +position);}
                    Cell financeCell = row.createCell(position);
                    if (figures.isSetFinance())
                    {
                        if (logger.isTraceEnabled()) {logger.trace("There are " +figures.getFinance().size() +" finances for " +figures.getLabel());}
                        for (Finance finance : figures.getFinance())
                        {
                            if (finance.getNr() == i)
                            {
                                financeCell.setCellStyle(numericStyle);
                                financeCell.setCellValue(finance.getValue());
                                if (logger.isTraceEnabled()){logger.trace("Set value to " +finance.getValue() +" for " +figures.getLabel());}
                            }	
                        }
                    }
                    if (figures.isSetTime())
                    {
                        for (Time time : figures.getTime())
                        {
                            if (time.getNr() == i)
                            {
                                financeCell.setCellStyle(dateStyle);
                                financeCell.setCellValue(time.getRecord().toGregorianCalendar().getTime());
                            }
                        }
                    }
                    position++;
		}
		
		// Recursevely call the next levels
		rowNr++;
            }
            else
            {
                logger.info("Now skipping " +parents.size() +"/" +(financeOffset-1));
            }
		
            parents.add(figures.getLabel());
            for (Figures nextLevel : figures.getFigures())
            {
                addContent(parents, nextLevel);
            }
	}

    public Workbook getWb() {return wb;}
    public void setWb(Workbook wb) {this.wb = wb;}
}