package org.jeesl.report.importers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShapeSimpleSerializableImporter <S extends Serializable, I extends ImportStrategy>
	extends AbstractShpImporter<S,I> {
	
	final static Logger logger = LoggerFactory.getLogger(ShapeSimpleSerializableImporter.class);
	
	public ShapeSimpleSerializableImporter(XlsSheet definition, File shapeFileDirectory) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
	    super(definition, shapeFileDirectory);
	}
	
	/*
	* Build a new Shape File Importer
	*
	* @return New instance of ShapeFileImporter
	*
	*/
	public static <S extends Serializable, C extends Serializable, I extends ImportStrategy>
	ShapeSimpleSerializableImporter<S,I> factory(XlsSheet definition, File shapeFileDirectory) throws IOException, ClassNotFoundException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
	    return new ShapeSimpleSerializableImporter<S,I>(definition, shapeFileDirectory);
	}
}
