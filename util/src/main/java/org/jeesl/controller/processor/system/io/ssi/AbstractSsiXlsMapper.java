package org.jeesl.controller.processor.system.io.ssi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.jeesl.controller.handler.io.log.LoggedExit;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.json.io.report.xlsx.JsonXlsCell;
import org.jeesl.model.json.io.report.xlsx.JsonXlsRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractSsiXlsMapper
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiXlsMapper.class);
	
	protected final Map<String,Integer> mapColumnCode;
	protected final Map<Integer,String> mapColumnIndex;
	protected final List<Integer> listColumns;
	
	public List<Integer> getListColumns() {return listColumns;}

	public AbstractSsiXlsMapper()
	{
		mapColumnCode = new HashMap<>();
		mapColumnIndex = new HashMap<>();
		listColumns = new ArrayList<>();
	}
	
	private void clear()
	{
		mapColumnCode.clear();
		mapColumnIndex.clear();
		listColumns.clear();
	}
	
	public void buildRangeColumnIndex(Row row, int indexStart, int indexEnd)
	{
		mapColumnCode.clear();
		for(int colIndex=indexStart;colIndex<=indexEnd;colIndex++)
		{
//			logger.info(String.format("%02d", colIndex)+"\t"+CellReference.convertNumToColString(colIndex));
			String key = row.getCell(colIndex).getStringCellValue().trim();
			logger.trace("i:"+colIndex+" "+key);
			logger.info(String.format("%02d", colIndex)+"\t"+CellReference.convertNumToColString(colIndex)+" "+key);
			mapColumnIndex.put(colIndex,key);
			if(mapColumnCode.containsKey(key)) {logger.warn("Already exists: "+key);}
			mapColumnCode.put(key,colIndex);
		}
	}
	
	public void buildColumnIndexFromStart(Row row, String colRef) {buildColumnIndexFromStart(row,CellReference.convertColStringToIndex(colRef));}
	public void buildColumnIndexFromStart(Row row, int indexStart)
	{
		this.clear();
		
		String key = row.getCell(indexStart).getStringCellValue().trim();
		while(ObjectUtils.isNotEmpty(key))
		{
			logger.info(indexStart+"\t"+CellReference.convertNumToColString(indexStart)+" "+key);
			mapColumnIndex.put(indexStart,key);
			
			if(mapColumnCode.containsKey(key)) {logger.warn("Already exists: "+key);}
			else
			{
				listColumns.add(indexStart);
				mapColumnCode.put(key,indexStart);
			}
			
			indexStart++;
			Cell cell = row.getCell(indexStart);
			if(cell==null) {key=null;}
			else
			{
				key = cell.getStringCellValue().trim();
			}
		}
	}
	
	public String toColumnCode(int index)
	{
		if(!mapColumnIndex.containsKey(index))
		{
			logger.warn("Column not available: "+index);
			LoggedExit.exit(true);
			return "";
		}
		else
		{
			return mapColumnIndex.get(index);
		}
	}
	
	public String toStringByKey(JsonXlsRow row, String key)
	{
		for(JsonXlsCell cell : row.getCells())
		{
			if(cell.getColumn()!=null && cell.getColumn().getKey()!=null && cell.getColumn().getKey().equals(key))
			{
				return cell.getValueString();
			}
		}
		return null;
	}
	
	public String toString(Row row, String header) throws UtilsConfigurationException
	{
		int index = mapColumnCode.get(header);
//		logger.info("Header "+header+" has index: "+index);
//		for(int i=0;i<8;i++) {logger.info(row.getCell(i).getStringCellValue().trim());}
		String value = row.getCell(index).getStringCellValue().trim();
		if(ObjectUtils.isEmpty(value)) {return null;}
		else return value;
	}
}