/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.ahtutils.report.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataUtil {
    
    final static Logger logger = LoggerFactory.getLogger(DataUtil.class);

    public static String getStringValue(Object object)
    	{
		// Since a simple "toString()" is not sufficient because the value can also be null, 
		// a more complex method has been chosen here to get a String representation of a cell value 
		String value = "";
		if (object.getClass().equals(String.class))
		{
			value = (String) object;
		}
		else if (object.getClass().equals(Date.class))
		{
			Date date = (Date) object;
			DateFormat df = SimpleDateFormat.getDateInstance();
			value = df.format(date);
		}
		else if (object.getClass().equals(Double.class))
		{
			Double doubleValue = (Double) object;
			value = doubleValue.toString();
		}
		else
		{
			value = "CELL IS NULL";
		}
		return value;
	}
    
    public static Object getCellValue(Cell cell)
	{
		Object value = new Object();
		
		// Prevent a NullPointerException
		if (cell != null)
		{
			if (cell.getHyperlink()!=null)
			{
				Workbook workbook = new XSSFWorkbook();
				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				Hyperlink link = cell.getHyperlink();
				String address = link.getAddress();
				if(logger.isTraceEnabled()){logger.trace("Found a Hyperlink to " +cell.getHyperlink().getAddress() +" in cell " +cell.getRowIndex() +"," +cell.getColumnIndex());}
				cell = evaluator.evaluateInCell(cell);
			}
			// Depending on the cell type, the value is read using Apache POI methods
			
			switch (cell.getCellTypeEnum()) {
			
				// String are easy to handle
				case STRING : 
					logger.trace("Found string " +cell.getStringCellValue());
					value = cell.getStringCellValue();
					break;
					
				// Since date formatted cells are also of the numeric type, this needs to be processed
				case NUMERIC:
					if (DateUtil.isCellDateFormatted(cell))
					{
						Date date = cell.getDateCellValue();
						DateFormat df = SimpleDateFormat.getDateInstance();
						logger.trace("Found date " +df.format(date));
						value = date;
					}
					else
					{
						logger.trace("Found general number " +cell.getNumericCellValue());
						value = cell.getNumericCellValue();
					}
					break;
				default: break;
			}
		}
		else
		{
			logger.trace("Found cell with NULL value");
		}
		return value;
	}
	
	public static Map<Short, String> debugRow(Sheet sheet, Integer rowIndex)
	{
		// Prepare the HashMap to return the data
		Map<Short, String> propertyColumnRelations = new HashMap<>();
		
		// Using a StringBuffer to create one line with all column titles
		StringBuffer sb = new StringBuffer();
		sb.append("Debugging Row " +rowIndex +" ... ");
		
		// Selecting first row since this should be the place where the column titles should be placed 
		Row firstRow    = sheet.getRow(rowIndex);
		
		// Iterating through all cells in first row
		for (short i = firstRow.getFirstCellNum() ; i < firstRow.getLastCellNum() ; i++)
		{
			Cell cell = firstRow.getCell(i);
			// Get the Cell Value as Object
			Object object = DataUtil.getCellValue(cell);
			
			// Get a String representation of the value
			String cellValue = getStringValue(object);
			
			// Add the content of the cell to StringBuffer
			sb.append("Column " +i +": '" +cellValue + "' ");
			
			// Add the result to the return Map
			propertyColumnRelations.put(i, cellValue);
		}
		
		// Show the StringBuffer content in logging
		logger.trace(sb.toString());
		
		return propertyColumnRelations;
	}
}
