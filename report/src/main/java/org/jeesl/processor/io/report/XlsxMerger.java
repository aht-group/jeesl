package org.jeesl.processor.io.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jeesl.factory.xlsx.io.report.XlsCellFactory;
import org.jeesl.factory.xlsx.io.report.XlsSheetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pjfanning.xlsx.StreamingReader;

public class XlsxMerger 
{
	final static Logger logger = LoggerFactory.getLogger(XlsxMerger.class);

	private int indexColumnStart,indexColumnEnd;
	private int sheetStart,sheetEnd;
	
	public static XlsxMerger instance() {return new XlsxMerger();}
	private XlsxMerger()
	{

	}
	
	public XlsxMerger columnRange(String from, String to)
	{
		indexColumnStart = CellReference.convertColStringToIndex(from);
		indexColumnEnd = CellReference.convertColStringToIndex(to);
		return this;
	}
	public XlsxMerger sheetRange(int sheetStart, int sheetEnd)
	{
		this.sheetStart = sheetStart;
		this.sheetEnd = sheetEnd;
		return this;
	}
	
	public void merge(Path pDirectory, File fOutput) throws IOException
	{
		this.merge(pDirectory.toFile(), fOutput);
	}
	public void merge(File fDir, File fOutput) throws IOException
	{
		Map<String,MutableInt> mapRowNr = new HashMap<>();
		
		SXSSFWorkbook wbOut = new SXSSFWorkbook(100);
		
		logger.info("Streaming "+fDir.getAbsolutePath());
		for(File f : fDir.listFiles())
		{
			logger.info(f.getAbsolutePath());
			if(f.getName().endsWith("xlsx") && !f.getName().startsWith("~$") && f.isFile()) 
			{
				for(int sheetIndex=sheetStart;sheetIndex<=sheetEnd;sheetIndex++)
				{
					InputStream is = new FileInputStream(f);
					Sheet sheetIn = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is).getSheetAt(sheetIndex);
					
					logger.info(sheetIndex+" "+sheetIn.getSheetName());
					int rowIndex=0;
					for (Row rowInt : sheetIn)
					{
						if(rowIndex>=2)
						{
							MutableInt totalRowNr = XlsSheetFactory.getRowNr(sheetIn.getSheetName()+"-total",mapRowNr);
							Sheet sheetOut = XlsSheetFactory.getSheet(wbOut,totalRowNr,sheetIn.getSheetName());
							MutableInt sheetRowNr = XlsSheetFactory.getRowNr(sheetOut.getSheetName(),mapRowNr);
							Row rowOut = sheetOut.createRow(sheetRowNr.intValue());
							for(int i=indexColumnStart;i<=indexColumnEnd;i++)
							{
								Cell cellIn = rowInt.getCell(i);
								if(cellIn!=null)
								{
									Cell cellOut = rowOut.createCell(i);
									XlsCellFactory.copy(cellIn, cellOut);
								}
							}
							totalRowNr.increment();
							sheetRowNr.increment();
						}
						rowIndex++;
					}
				}
				
			}
		}
		wbOut.write(new FileOutputStream(fOutput));
		wbOut.dispose();
	}
}