package org.jeesl.controller.processor.system.io.ssi;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.jeesl.model.json.system.io.report.xlsx.JsonXlsCell;
import org.jeesl.model.json.system.io.report.xlsx.JsonXlsRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractXlsMapper
{
	final static Logger logger = LoggerFactory.getLogger(AbstractXlsMapper.class);
	
	protected final Map<String,Integer> mapColumnCode;
	protected final Map<Integer,String> mapColumnIndex;
	
	public AbstractXlsMapper()
	{
		mapColumnCode = new HashMap<>();
		mapColumnIndex = new HashMap<>();
	}
	
	public void buildColumnIndex(Row row, int max)
	{
		mapColumnCode.clear();
		for(int colIndex=0;colIndex<=max;colIndex++)
		{
			String key = row.getCell(colIndex).getStringCellValue().trim();
			logger.trace("i:"+colIndex+" "+key);
			logger.trace(colIndex+"\t"+CellReference.convertNumToColString(colIndex)+" "+key);
			mapColumnIndex.put(colIndex,key);
			if(mapColumnCode.containsKey(key)) {logger.warn("Already exists: "+key);}
			mapColumnCode.put(key,colIndex);
		}
	}
	
	public String toColumnCode(int index)
	{
		if(!mapColumnIndex.containsKey(index))
		{
			logger.warn("Column not available: "+index);
			System.exit(-1);
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
}