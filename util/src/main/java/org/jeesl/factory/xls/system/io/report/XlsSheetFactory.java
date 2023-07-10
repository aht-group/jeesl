package org.jeesl.factory.xls.system.io.report;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsSheetFactory
{
	final static Logger logger = LoggerFactory.getLogger(XlsSheetFactory.class);
		
	public static Sheet getSheet(Workbook wb, String sheetName)
	{
		Sheet sheet;
		if (wb.getSheet(sheetName) == null){sheet = wb.createSheet(sheetName);}
		else {sheet = wb.getSheet(sheetName);}
		return sheet;
	}
	public static Sheet getSheet(Workbook wb, MutableInt rowNr, String sheetName)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(sheetName);
		
		Double d = Double.valueOf(rowNr.doubleValue() / 1000000);
		Integer i= d.intValue();
		if(i>0) {sb.append("").append(i);}
		
		String sheetCode = sb.toString();
		Sheet sheet;
		if (Objects.isNull(wb.getSheet(sheetCode))) {sheet = wb.createSheet(sheetCode);}
		else {sheet = wb.getSheet(sheetCode);}
		return sheet;
	}
	
	
	public static MutableInt getRowNr(String sheetName, Map<String,MutableInt> map)
	{
		if(!map.containsKey(sheetName)) {map.put(sheetName,new MutableInt(0));}
		return map.get(sheetName);
	}
}