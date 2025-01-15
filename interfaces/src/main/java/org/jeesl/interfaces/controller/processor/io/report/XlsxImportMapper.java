package org.jeesl.interfaces.controller.processor.io.report;

import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface XlsxImportMapper
{
	final static Logger logger = LoggerFactory.getLogger(XlsxImportMapper.class);
	
	boolean isAnalyseHeader();
	void buildRangeColumnIndex(Row row, int indexStart, int indexEnd);
}