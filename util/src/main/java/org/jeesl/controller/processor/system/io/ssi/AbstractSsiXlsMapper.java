package org.jeesl.controller.processor.system.io.ssi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.exlp.controller.handler.io.log.LoggedExit;
import org.exlp.util.system.DateUtil;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.controller.processor.io.report.XlsxImportMapper;
import org.jeesl.model.json.io.report.xlsx.JsonXlsCell;
import org.jeesl.model.json.io.report.xlsx.JsonXlsRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractSsiXlsMapper implements XlsxImportMapper
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiXlsMapper.class);
	
	protected final Map<String,Integer> mapColumnCode;
	protected final Map<Integer,String> mapColumnIndex;
	protected final List<Integer> listColumns;
	
	public List<Integer> getListColumns() {return listColumns;}
	
	private boolean analyseHeader; public boolean isAnalyseHeader() {return analyseHeader;} public void setAnalyseHeader(boolean analyseHeader) {this.analyseHeader = analyseHeader;}

	public AbstractSsiXlsMapper()
	{
		mapColumnCode = new HashMap<>();
		mapColumnIndex = new HashMap<>();
		listColumns = new ArrayList<>();
		
		analyseHeader = true;
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
	
	public String toStringByCol(Row row, String col) throws UtilsConfigurationException {return this.toString(row, CellReference.convertColStringToIndex(col));}
	public String toString(Row row, int index) throws UtilsConfigurationException
	{
		if(Objects.isNull(row)) {return null;}
		if(Objects.isNull(row.getCell(index))) {logger.warn("NULL: "+row.getRowNum()+":"+index); return null;}
		String value = row.getCell(index).getStringCellValue().trim();
		if(ObjectUtils.isEmpty(value)) {return null;}
		else return value;
	}
	public Double toDouble(Row row, String col) throws UtilsConfigurationException {return this.toDouble(row, CellReference.convertColStringToIndex(col));}
	public Double toDouble(Row row, int index) throws UtilsConfigurationException
	{
		if(Objects.isNull(row)) {return null;}
		if(Objects.isNull(row.getCell(index))) {logger.warn("NULL: "+row.getRowNum()+":"+index); return null;}
		Double value = row.getCell(index).getNumericCellValue();
		if(ObjectUtils.isEmpty(value)) {return null;}
		else return value;
	}
	
	public Integer toIntegerByCol(Row row, String col) throws UtilsConfigurationException {return this.toInteger(row, CellReference.convertColStringToIndex(col));}
	public Integer toInteger(Row row, int index) throws UtilsConfigurationException
	{
		if(Objects.isNull(row)) {return null;}
		if(Objects.isNull(row.getCell(index))) {logger.warn("NULL: "+row.getRowNum()+":"+index); return null;}
		Double value = row.getCell(index).getNumericCellValue();
		if(ObjectUtils.isEmpty(value)) {return null;}
		else return value.intValue();
	}
	
	public Long toLongByCol(Row row, String col) throws UtilsConfigurationException {return this.toLong(row, CellReference.convertColStringToIndex(col));}
	public Long toLong(Row row, int index) throws UtilsConfigurationException
	{
		if(Objects.isNull(row)) {return null;}
		if(Objects.isNull(row.getCell(index))) {logger.warn("NULL: "+row.getRowNum()+":"+index); return null;}
		Double value = row.getCell(index).getNumericCellValue();
		if(ObjectUtils.isEmpty(value)) {return null;}
		else return value.longValue();
	}
	
	public LocalDate toDateByCol(Row row, String col) throws UtilsConfigurationException {return this.toDate(row, CellReference.convertColStringToIndex(col));}
	public LocalDate toDate(Row row, int index) throws UtilsConfigurationException
	{
		if(Objects.isNull(row)) {return null;}
		if(Objects.isNull(row.getCell(index))) {logger.warn("NULL: "+row.getRowNum()+":"+index); return null;}
		Date value = row.getCell(index).getDateCellValue();
		if(ObjectUtils.isEmpty(value)) {return null;}
		else return DateUtil.toLocalDate(value);
	}
}