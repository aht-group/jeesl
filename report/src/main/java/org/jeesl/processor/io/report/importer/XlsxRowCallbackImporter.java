package org.jeesl.processor.io.report.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.json.system.io.report.xls.JsonXlsCellFactory;
import org.jeesl.factory.json.system.io.report.xls.JsonXlsColumnFactory;
import org.jeesl.interfaces.controller.processor.io.report.XlsxImportCallback;
import org.jeesl.interfaces.controller.processor.io.report.XlsxImportMapper;
import org.jeesl.model.json.io.report.xlsx.JsonXlsCell;
import org.jeesl.model.json.io.report.xlsx.JsonXlsColumn;
import org.jeesl.model.json.io.report.xlsx.JsonXlsRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitorjbl.xlsx.StreamingReader;

public class XlsxRowCallbackImporter
{
	final static Logger logger = LoggerFactory.getLogger(XlsxRowCallbackImporter.class);
	
	protected final Configuration config;
	
	private File xlsDir;
	private File xlsFile;
	
	protected int indexSheet;
	protected int indexColumnStart, indexColumnEnd;
	protected int indexRowStart; public void setIndexRowStart(int indexRowStart) {this.indexRowStart = indexRowStart;}
	
	private XlsxImportCallback callback; public void callback(XlsxImportCallback callback) {this.callback=callback;}
	private XlsxImportMapper mapper; public void mapper(XlsxImportMapper mapper) {this.mapper=mapper;}
	
	public XlsxRowCallbackImporter(Configuration config)
	{
		this.config=config;	
	}
	
	public void readDirFromConfig(String code)
	{
		xlsDir = new File(config.getString(code));
	}
	
	public void restrictArea(int rowStart, String columnStart, String columnEnd)
	{
		this.restrictArea(rowStart, CellReference.convertColStringToIndex(columnStart), CellReference.convertColStringToIndex(columnEnd));
	}
	public void restrictArea(int rowStart, int columnStart, int columnEnd)
	{
		indexRowStart = rowStart;
		indexColumnStart = columnStart;
		indexColumnEnd = columnEnd;
		
		indexSheet = 0;
	}
	
	public void streamDir() throws IOException, UtilsConfigurationException
	{
		logger.info("Streaming "+xlsDir.getAbsolutePath());
		for(File f : xlsDir.listFiles())
		{
			logger.info(f.getAbsolutePath());
			if(f.getName().endsWith("xlsx") && !f.getName().startsWith("~$") && f.isFile()) 
			{
				this.xlsFile=f;
				streamFile();
			}
		}
	}
	
	public void streamFile(Path path) throws IOException, UtilsConfigurationException
	{
		logger.debug("Streaming "+path.toString());
		this.xlsFile = path.toFile();
		this.streamFile();
	}
	
	protected void streamFile() throws IOException, UtilsConfigurationException
	{
		String fName = FilenameUtils.removeExtension(xlsFile.getName());
		logger.info(fName);
			
		InputStream is = new FileInputStream(xlsFile);
		Sheet sheet = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is).getSheetAt(indexSheet);
		
		int rowIndex=0;
		for (Row r : sheet)
		{
			rowIndex++;
			
			if(rowIndex==1)
			{
				if(Objects.nonNull(mapper) && mapper.isAnalyseHeader()) {mapper.buildRangeColumnIndex(r,0,indexColumnEnd);}
			}
			
			
			
			if(rowIndex>=indexRowStart)
			{
				if(Objects.isNull(callback)) {throw new UtilsConfigurationException("No Callback");}
				
				logger.trace("Processing Index:{} Start:{}",rowIndex,indexRowStart);
				callback.callbackXlsxRow2Mongo(mapper,fName,indexSheet,rowIndex,r);
			}
		}
//		indexRowStart = 2;
		is.close();
	}
	
	public JsonXlsRow toJsonRow(int rowIndex, Row row)
	{
		JsonXlsRow jRow = new JsonXlsRow();
		jRow.setCells(new ArrayList<JsonXlsCell>());
		jRow.setNr(rowIndex);
		for(int i=indexColumnStart;i<=indexColumnEnd;i++)
		{
			Cell cell = row.getCell(i);
			if(Objects.nonNull(cell))
			{
				JsonXlsColumn jColumn = JsonXlsColumnFactory.build();
				jColumn.setNr(i);
				jColumn.setKey(CellReference.convertNumToColString(i));
				
				JsonXlsCell jCell = JsonXlsCellFactory.build(cell);
				jCell.setColumn(jColumn);
				jRow.getCells().add(jCell);
			}
		}
		return jRow;
	}
}