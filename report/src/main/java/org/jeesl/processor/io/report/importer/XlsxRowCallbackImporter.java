package org.jeesl.processor.io.report.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.controller.processor.system.io.ssi.AbstractSsiXlsMapper;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.controller.processor.io.report.XlsxToMongoCallback;
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
	
	private XlsxToMongoCallback callback; public void callback(XlsxToMongoCallback callback) {this.callback=callback;}
	private AbstractSsiXlsMapper mapper; public void mapper(AbstractSsiXlsMapper mapper) {this.mapper=mapper;}
	
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
		indexRowStart=rowStart;
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
	
	protected void streamFile() throws IOException, UtilsConfigurationException
	{
		String fName = FilenameUtils.removeExtension(xlsFile.getName());
		logger.info(fName);
		
//		try {district = fUtils.fByName(MeisDistrict.class, fName.toUpperCase());}
//		catch (JeeslNotFoundException e) {throw new UtilsConfigurationException("No district with name="+fName);}
//		logger.info("Open: "+xlsFile.getAbsolutePath()+" and processing "+TxtDistrictFactory.hierarchy(district));
		
		InputStream is = new FileInputStream(xlsFile);
		Sheet sheet = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is).getSheetAt(indexSheet);
		
		int rowIndex=0;
		for (Row r : sheet)
		{
			rowIndex++;
			
			if(rowIndex==1)
			{
				if(Objects.isNull(mapper)) {throw new UtilsConfigurationException("No Mapper");}
				mapper.buildRangeColumnIndex(r,0,indexColumnEnd);
			}
			if(rowIndex>=indexRowStart)
			{
				if(Objects.isNull(callback)) {throw new UtilsConfigurationException("No Callback");}
				callback.callbackXlsxRow2Mongo(fName,indexSheet,rowIndex,r);
			}
		}
		indexRowStart = 2;
		is.close();
	}
}