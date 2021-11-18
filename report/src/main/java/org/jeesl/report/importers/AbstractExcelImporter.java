package org.jeesl.report.importers;

import org.jeesl.report.importers.AbstractTableImporter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import net.sf.ahtutils.interfaces.controller.report.UtilsXlsDefinitionResolver;
import net.sf.ahtutils.report.util.DataUtil;
import net.sf.ahtutils.xml.report.XlsSheet;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import org.apache.commons.lang3.StringUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeesl.api.controller.ImportStrategy;
import static org.jeesl.report.importers.AbstractShpImporter.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractExcelImporter <C extends Serializable, I extends ImportStrategy> extends AbstractTableImporter<C, I>{
	
    final static Logger logger = LoggerFactory.getLogger(AbstractExcelImporter.class);

    protected File                       excelFile;
    protected Integer                    startRow;
    protected XSSFWorkbook               workbook;
    protected Sheet                      activeSheet;

    public AbstractExcelImporter(UtilsXlsDefinitionResolver resolver, String reportCode, InputStream is) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
    {
        this(resolver.definition(reportCode).getXlsSheet().get(0),is);
    }

    public AbstractExcelImporter(UtilsXlsDefinitionResolver resolver, String reportCode, String filename) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
    {
        this(resolver.definition(reportCode).getXlsSheet().get(0),filename);
    }

    public AbstractExcelImporter(XlsSheet definition, InputStream is) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
    {
        super(definition);

        // Read Excel workbook from given file(name)
        this.workbook       = new XSSFWorkbook(is);
    }

    public AbstractExcelImporter(XlsSheet definition, String filename) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
    {
        super(definition);

        // Prepare file to be read
        this.excelFile      = new File(filename);
        FileInputStream fis = new FileInputStream(excelFile);

        // Read Excel workbook from given file(name)
        this.workbook       = new XSSFWorkbook(fis);
    }

    @Override
    public void rangeCheck(Boolean skipTitle)
    {
        logger.debug("Performing Range check");
        // Define the rows to begin with and to end with, whether with or without first row
        endRow   = (short) activeSheet.getLastRowNum();
        startRow = activeSheet.getFirstRowNum();
        if (skipTitle) {startRow++;}
        // Take the first row as a reference of how many columns are there
        startColumn = activeSheet.getRow(activeSheet.getFirstRowNum()).getFirstCellNum();
        endColumn   = activeSheet.getRow(activeSheet.getFirstRowNum()).getLastCellNum();
        logger.info("Table columns are from " +startColumn  + " to " +endColumn);
        logger.info("Table rows    are from " +startRow     + " to " +endRow);
    }

    // Select a sheet from the Excel workbook by name
    public void selectSheetByName(String name) {activeSheet = workbook.getSheet(name);}

    // Select a sheet from the Excel workbook by name
    public void selectFirstSheet()             {activeSheet = workbook.getSheetAt(0);}

    // Debugging of sheet data
    public void debugHeader()       {DataUtil.debugRow(activeSheet, 0);}
    public void debugFirstRow()     {DataUtil.debugRow(activeSheet, 1);}
        	
	
    
    @Override
    public Object getCellValue(short row, short column)
    {
        Cell cell = activeSheet.getRow(row).getCell(column);
        if (logger.isTraceEnabled()){logger.trace("Value of " +row +"/" +column + " is " +DataUtil.getCellValue(cell).toString());}
        return DataUtil.getCellValue(cell);
    }
	 
    @Override
    public boolean rowExists(short row)
    {
        boolean rowExists = true;
        Row excelRow = activeSheet.getRow(row);
        if (excelRow == null) 
        {
            logger.warn("There is no row " +row +" in " +activeSheet.getSheetName());
            return false;
        }
        for(int cellNum = excelRow.getFirstCellNum(); cellNum < excelRow.getLastCellNum(); cellNum++)
        {
           Cell cell = excelRow.getCell(cellNum);
           if(cell != null && cell.getCellTypeEnum() != CellType.BLANK && StringUtils.isNotBlank(cell.toString()))
           {
                rowExists = true;
           }    
        }
        if (!rowExists) 
        {
            logger.warn("Row " +excelRow.getRowNum() + " seems to be empty and will be ignored!");
        }
        return rowExists;
    }
    
    @Override
    public Map<Short, String> getColumnTitles()
    {
        logger.info("Properties and Columns");
	logger.info("======================");
	Map<Short, String> propertyNameForColumn = DataUtil.debugRow(activeSheet, 0);
	for (Short column : propertyNameForColumn.keySet())
	{
	    logger.info("Column " +column + " stores " +propertyNameForColumn.get(column));
	}
	return propertyNameForColumn;
    }
    
    @Override
    public Map<Short, String> getPreview()
    {
        logger.info("Properties and Columns");
	logger.info("======================");
	Integer row = 1;
	Map<Short, String> propertyNameForColumn = DataUtil.debugRow(activeSheet, row);
	for (Short column : propertyNameForColumn.keySet())
	{
	    logger.info("Column " +column + " stores " +propertyNameForColumn.get(column));
	}
	return propertyNameForColumn;
    }
}
