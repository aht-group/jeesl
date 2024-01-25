package net.sf.ahtutils.report.revert;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import net.sf.ahtutils.interfaces.controller.report.UtilsXlsDefinitionResolver;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import net.sf.exlp.util.xml.JaxbUtil;

import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.model.xml.io.report.Media;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.jeesl.model.xml.io.report.XlsColumn;
import org.jeesl.model.xml.io.report.XlsDefinition;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.jeesl.model.xml.io.report.XlsTransformation;
import org.jeesl.model.xml.io.report.XlsWorkbook;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AhtUtilsXlsDefinitionResolver implements UtilsXlsDefinitionResolver
{
	
	final static Logger logger = LoggerFactory.getLogger(AhtUtilsXlsDefinitionResolver.class);

	private final String reportFileLocation    = "reports.ahtutils-report/reports.xml";
	private final String excelDefFilesLocation = "reports.ahtutils-report/xlsx";
	private final Boolean isGeneric;
	private XlsWorkbook genericWorkbook;
	
	public AhtUtilsXlsDefinitionResolver (Class<?> c)
	{
		logger.debug("Using generic strategy");
		isGeneric = true;
		genericWorkbook = new XlsWorkbook();
		
		XlsSheet  sheet  = new XlsSheet();
		sheet.setQuery("/oList");
		char columnLetter='A';

		for(Field f : c.getDeclaredFields())
        {
			JsonProperty p = f.getAnnotation(JsonProperty.class);
			logger.trace("Field " +f.getName() +" will be inspected.");
			if (p!=null)
			{
				logger.info("Found an annotated column");
				XlsColumn column = new XlsColumn();
				column.setColumn(columnLetter +"");
				columnLetter++;
				XlsTransformation transformation = new XlsTransformation();
				String xPath = "";
				if (p.value().length()>1)
				{
					xPath = Character.toLowerCase(p.value().charAt(0)) + p.value().substring(1); 
				}
				else
				{
					xPath = xPath +Character.toLowerCase(p.value().charAt(0));
				}
				
				transformation.setBeanProperty(xPath);
				transformation.setDataClass("String");
				transformation.setFormatPattern("");

				column.setXlsTransformation(transformation);

				Langs langs = new Langs();
				Lang  lang  = new Lang();
				lang.setKey("en");
				lang.setTranslation(p.value());
				langs.getLang().add(lang);
				column.setLangs(langs);

				sheet.getContent().add(column);
			}
			
		}
		genericWorkbook.getXlsSheet().add(sheet);
	}
	
	public AhtUtilsXlsDefinitionResolver ()
	{
		isGeneric = false;
	}
	
	
	
	@Override
	public XlsWorkbook definition(String code)
	{
		if (isGeneric)
		{
			logger.debug("Returning generic Workbook definition.");
			return genericWorkbook;
		}
		else
		{
			// Initialize the MultiResourceLoader for use with filesystem or classpath resources
			MultiResourceLoader mrl = MultiResourceLoader.instance();

			// Load the reports.xml definition from given source
			Reports       reports       = null;
			try {
				reports = JaxbUtil.loadJAXB(mrl.searchIs(reportFileLocation), Reports.class);
			} catch (FileNotFoundException ex) {
				logger.error(ex.getMessage());
			}

			// Find the report information by code
			Report        report        = null;
			try {
				report = ReportXpath.getReport(reports, code);
			} catch (ExlpXpathNotFoundException ex) {
				logger.error(ex.getMessage());
			} catch (ExlpXpathNotUniqueException ex) {
				logger.error(ex.getMessage());
			}

			// Find the information on where the XML containing the XlsDefinition (and workbook) for the report is located
			String        filename      = "";
			for (Media m : report.getMedia())
			{
				if (m.getType().equals("xls"))
				{
					filename = excelDefFilesLocation +"/" +m.getJr().get(0).getName() +".xml";
				}
			}

			// Load the definition
			XlsDefinition def = null;
			try {
				def = JaxbUtil.loadJAXB(mrl.searchIs(filename), XlsDefinition.class);
			} catch (FileNotFoundException ex) {
				logger.error(ex.getMessage());
			}

			// Return the first workbook since there should be only one in the referenced XML file
			return def.getXlsWorkbook().get(0);
		}
	}
	
	
}
