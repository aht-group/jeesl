package org.jeesl.interfaces.controller.processor.io.report;

import org.apache.poi.ss.usermodel.Row;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface XlsxImportCallback
{
	final static Logger logger = LoggerFactory.getLogger(XlsxImportCallback.class);
	
//	void x();
	void callbackXlsxRow2Mongo(XlsxImportMapper xm, String fileName, int indexSheet, int indexRow, Row row) throws UtilsConfigurationException;
}