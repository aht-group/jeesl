package net.sf.ahtutils.report;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.jxpath.JXPathContext;
import org.jdom2.Namespace;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import net.sf.ahtutils.report.exception.ReportException;
import net.sf.ahtutils.xml.report.Jr;
import net.sf.ahtutils.xml.report.Media;
import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.report.Resource;
import net.sf.ahtutils.xml.report.Resources;
import net.sf.ahtutils.xml.report.Templates;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import net.sf.exlp.util.xml.JDomUtil;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRVirtualizationContext;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRConcurrentSwapFile;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;

/*
 * @author helgehemmer
 *
 */
public class ReportHandler
{
	
	final static Logger logger = LoggerFactory.getLogger(ReportHandler.class);
	protected Reports reports;
	protected Resources resources;
	protected Templates templates;
	protected String reportRoot;
	protected String configFileLocation;
	protected MultiResourceLoader mrl;	
	
	public enum Format {pdf, xls};
	
	/*
	 * This class contains methods to work with the elements configured in reports.xml,
	 * resources.xml and templates.xml - Additional functionality needs to be implemented
	 * in dedicated classes (e.g. working with specific data XML files, report)
	 * @param reportFile Name of the configuration file
	 * @throws ReportException
	 */
	public ReportHandler(String reportFile) throws ReportException
	{
		logger.info("Initializing report handling system");
		mrl = new MultiResourceLoader();
		configFileLocation = reportFile.substring(0, reportFile.lastIndexOf("/")) +"/";
		
		try
		{
			reports = JaxbUtil.loadJAXB(reportFile, Reports.class);
//			JaxbUtil.debug2(reports);
		}
		catch (FileNotFoundException e)
		{
			throw new ReportException("Problem loading configuration file: " +e.getMessage());
		}
		
		//Reading reportRoot directory from configuration file and add all directories covered by the conventions of the AHTUtils reporting system
		reportRoot = reports.getDir() +"/";
		mrl.addPath(reports.getDir());
		mrl.addPath(configFileLocation);
		mrl.addPath("src/main/resource/" +reportRoot);
		mrl.addPath("src/main/" +reportRoot);
		
		try {mrl.searchIs("reports.xml");}
		catch (FileNotFoundException ee) {throw new ReportException("Error: reportRoot " +reportRoot +" does not exist!");}
			
		
		logger.info("Read report configuration from " +configFileLocation +" containing " +reports.getReport().size() +" reports.");
		
		//Reading resources file from reportRoot if available
		try
		{
			resources = (Resources)JaxbUtil.loadJAXB(reportRoot +"resources.xml", Resources.class);
			logger.info("Read resources configuration from " +reportRoot +"resources.xml");
		}
		catch (FileNotFoundException e)
		{
			resources = new Resources();
			logger.warn("Problem loading resources.xml from " +reportRoot +" - no images etc. needed?");
		}
		
		//Reading resources file from reportRoot if available
		try
		{
			templates = (Templates)JaxbUtil.loadJAXB(reportRoot +"templates.xml", Templates.class);
			logger.info("Read templates definitions from " +reportRoot +"templates.xml");
		}
		catch (FileNotFoundException e) {
			logger.warn("Problem loading templates.xml from " +reportRoot +" - no templates needed?");
		}
	}
	
	/*
	 * This class contains methods to work with the elements configured in reports.xml, resources.xml and templates.xml - Additional functionality needs to be implemented in dedicated classes (e.g. working with specific data XML files, report)
	 * @param reports Reports object
	 * @param resources Resources object
	 * @param templates Templates object
	 * @throws ReportException
	 */
	public ReportHandler(Reports reports, Resources resources,Templates templates) throws ReportException
	{
		logger.info("Initializing report handling system with given configuration objects");
		this.reports   = reports;
		this.resources = resources;
		this.templates = templates;
	}

	/*
	 * This class contains methods to work with the elements configured in reports.xml, resources.xml and templates.xml - Additional functionality needs to be implemented in dedicated classes (e.g. working with specific data XML files, report)
	 * @param reports
	 * @throws ReportException
	 */
	public ReportHandler(Reports reports) throws ReportException
	{
		logger.info("Initializing report handling system with given reports object");
		mrl = new MultiResourceLoader();
		
		this.reports = reports;
		
		//Reading reportRoot directory from configuration file and add all directories covered by the conventions of the AHTUtils reporting system
		reportRoot = reports.getDir() +"/";
		mrl.addPath(reports.getDir());
		mrl.addPath("src/main/resource/" +reportRoot);
		mrl.addPath("src/main/" +reportRoot);
		
		//Reading resources file from reportRoot if available
		resources = new Resources();
		try
		{
			resources = JaxbUtil.loadJAXB(mrl.searchIs(reports.getResources()), Resources.class);
			logger.info("Read resources configuration from " +reports.getResources());
		}
		catch (FileNotFoundException e)
		{
			logger.warn("Problem loading resources.xml from " +reports.getResources() +" - no images etc. needed?");
		}
		
		//Reading resources file from reportRoot if available
		try
		{
			templates = (Templates)JaxbUtil.loadJAXB(mrl.searchIs(reports.getTemplates()), Templates.class);
			logger.info("Read templates definitions from " +reports.getTemplates());
		}
		catch (FileNotFoundException e)
		{
			logger.warn("Problem loading templates.xml from " +reports.getTemplates() +" - no templates needed?");
		}
	}
	
	/*
	 * Validating the report resources increases performance hence no compilation logic is executed without the needed files
	 * @throws ReportException
	 */
	public void validateReportResources() throws ReportException
	{
		//Checking for report layout files
		ArrayList<String> missingReports = new ArrayList<String>(); 
		for (Report report : reports.getReport())
		{
			for (Media media : report.getMedia())
			{
				if (media.getType().equals("pdf"))
				{
					for (Jr jasperReport : media.getJr())
					{
						String filename     = jasperReport.getType() +jasperReport.getName() +".jrxml";
						String fileLocation = "jrxml/" +report.getDir() +"/" +media.getType() +"/" +filename;
						try {
							mrl.searchIs(fileLocation);
						} catch (FileNotFoundException e1) {
							missingReports.add(fileLocation);
							logger.warn("File not found!" +e1.getMessage());
						}
					}
				}
			}
		}
		if (!missingReports.isEmpty())
		{
			throw new ReportException("Validation of resources failed - " +missingReports.size() +" reports are missing: " +missingReports.toString());
		}
	}
	
	/*
	 * Get the master report associated with the given report. The JasperDesign object will be loaded from a .jrxml file. 
	 * @param id
	 * @param subreport
	 * @param format
	 * @param type
	 * @return 
	 * @throws ReportException
	 */
	public JasperDesign getReport(String id, String subreport, String format, String type) throws ReportException
	{
		Jr jr = null;
		try
		{
			if (type.equals("sr"))
			{
				// THIS IS WRONG!
				jr = ReportXpath.getSr(reports, id, subreport, format);
			}
			else
			{
				jr = ReportXpath.getMr(reports, id, format);
			}
			
		}
		 catch (ExlpXpathNotFoundException e1) {
			throw new ReportException("XPath has not been found when trying to find report with id " +id +"! " +e1.getMessage());
		} catch (ExlpXpathNotUniqueException e2) {
			throw new ReportException("XPath search found non-unique results when trying to find report with id " +id +"! " +e2.getMessage());
		}
		
		String reportDir = (String)JXPathContext.newContext(reports).getValue("report[@id='"+ id +"']/@dir");
		String location = "jrxml";
		if (jr.isSetAlternateDir())
		{
			location = location +"/" +jr.getAlternateDir() +"/" +format +"/" +type +jr.getName() +".jrxml";
		}
		else
		{
			location = location +"/" +reportDir +"/" +format +"/" +type +jr.getName() +".jrxml";
		}
		JasperDesign design = null;
		try
		{
			design = (JasperDesign)JRXmlLoader.load(mrl.searchIs(location));
		} catch (FileNotFoundException e) {
			throw new ReportException("Requested report design jrxml file for report " +id +" could not be found at " +location +"!");
		} catch (JRException e) {
			throw new ReportException("Internal JasperReports error when trying to load requested report design jrxml file for report " +id +": " +e.getMessage());
		}
		logger.info("lodaded report");
		return design;
	}
	
	/*
	 * Compile a JasperDesign to a JasperReport
	 * @param jasperDesign
	 * @return 
	 * @throws ReportException
	 */
	@Deprecated
	public JasperReport getCompiledReport(JasperDesign jasperDesign) throws ReportException
	{
		JasperReport report;
		try {
			report = JasperCompileManager.compileReport(jasperDesign);
		} catch (JRException e) {
			throw new ReportException("Internal JasperReports error when compiling JasperDesign to JasperReport: " +e.getMessage());
		}
		return report;
	}
	
	/*
	 * Compile a JasperDesign to a JasperReport
	 * @param reportId
	 * @param subreport
	 * @param format
	 * @param type
	 * @return 
	 * @throws ReportException
	 */
	public JasperReport getCompiledReport(String reportId, String subreport, String format, String type) throws ReportException
	{
		Jr report = null;
		try
		{
			if (subreport != null)
			{
				report = ReportXpath.getSr(reports, reportId, subreport, format);
			}
			else
			{
				report = ReportXpath.getMr(reports, reportId, format);
			}
		}
		catch (ExlpXpathNotFoundException e1) {throw new ReportException("XPath has not been found when trying to find report with id " +reportId +"! " +e1.getMessage());}
		catch (ExlpXpathNotUniqueException e1) {throw new ReportException("XPath search found non-unique results when trying to find report with id " +reportId +"! " +e1.getMessage());}
		
		String reportDir = (String)JXPathContext.newContext(reports).getValue("report[@id='"+ reportId +"']/@dir");
		String location = "jasper";
		if (report.isSetAlternateDir())
		{
			location = location +"/" +report.getAlternateDir() +"/" +format +"/ltr/"  +type +report.getName() +".jasper";
		}
		else
		{
			location = location +"/" +reportDir +"/" +format +"/ltr/"  +type +report.getName() +".jasper";
		}
		
		JasperReport reportCompiled = null;
		try
		{
			reportCompiled = (JasperReport)JRLoader.loadObject(mrl.searchIs(location));
			logger.info("Compiled Report found at "+location);
		}
		catch (FileNotFoundException e)
		{
			logger.warn("Requested compiled report jasper file for report " +reportId +" could not be found at " +location +"! - Trying to recompile it from jrxml.");
			JasperDesign design = getReport(reportId, subreport, format, type);
			reportCompiled      = (JasperReport) getCompiledReport(design);
		}
		catch (JRException e)
		{
			throw new ReportException("Internal JasperReports error when trying to get requested compiled report design for report " +reportId +": " +e.getMessage());
		}
		return reportCompiled;
	}
	
	/*
	 * Get a Map of all standard parameters plus the given locale and the included data XML file 
	 * @param doc
	 * @param locale
	 * @return 
	 */
	@Deprecated
	public Map<String,Object> getParameterMap(Document doc, Locale locale)
	{	
		Map<String,Object> mapReportParameter = new HashMap<String,Object>();
		
		//Set standard parameters
		mapReportParameter.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, doc);
		mapReportParameter.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
		mapReportParameter.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.00");
		mapReportParameter.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
		mapReportParameter.put(JRParameter.REPORT_LOCALE, locale);
		mapReportParameter.put("REPORT_LOCALE", locale);
		
		//Add all resources configured in resources.xml
		for (Resource res : resources.getResource())
		{
                    logger.info("Adding resource of type " +res.getType() +" with id='" +res.getName() +"' loaded from " +res.getValue().getValue());
                    if (res.getType().equals("image"))
				
				{
					BufferedImage image = null;
					try {
						String imgLocation = "/resources/" +res.getType() +"/" +res.getValue().getValue();
						logger.info("Including image resource: " +imgLocation);
						image = ImageIO.read(mrl.searchIs(imgLocation));} 
					catch (FileNotFoundException e) {logger.error(e.getMessage());}
					catch (IOException e) {logger.error(e.getMessage());}
					mapReportParameter.put(res.getName(), image);
				}
                        if (res.getType().equals("template"))
                                
				{
                                    
					try {
						String templateLocation = "/resources/templates" +"/" +res.getValue().getValue();
						logger.info("Including style template resource: " +templateLocation);
						JRTemplate style = JRXmlTemplateLoader.load(mrl.searchIs(templateLocation));
                                                mapReportParameter.put(res.getName() +"-style", style);
                                        }
					catch (FileNotFoundException e) {logger.error(e.getMessage());}
					catch (IOException e) {logger.error(e.getMessage());}
					
				}
		}
		for (Object key : mapReportParameter.keySet())
		{
			String keyString = (String) key;
			String valueString = mapReportParameter.get(keyString).toString();
			logger.info("Report Parameter: " +keyString +" = " +valueString);
		}
		return mapReportParameter;
	}
	
	/*
	 * Get a Map of all standard parameters plus the given locale and the included data XML file 
	 * @param doc
	 * @param locale
	 * @return 
	 */
	public Map<String,Object> getParameterMapJDom(org.jdom2.Document doc, Locale locale) 
	{	
		Map<String,Object> mapReportParameter = new HashMap<String,Object>();
		
		//Set standard parameters
		mapReportParameter.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
		mapReportParameter.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.00");
		mapReportParameter.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
		mapReportParameter.put(JRParameter.REPORT_LOCALE, locale);
		mapReportParameter.put("REPORT_LOCALE", locale);
		
		//Add charts if included in report info block
		
		//Get the root element (report)
		org.jdom2.Element reportElement = doc.getRootElement();
		
		//Get the info element as child of report element
		org.jdom2.Element infoElement   = reportElement.getChild("info", Namespace.getNamespace("http://ahtutils.aht-group.com/report"));
		if (infoElement!=null)
		{
			
			
			logger.warn("Chart module deactivated");
//			Info info = JDomUtil.toJaxb(infoElement, Info.class);
//			OfxChartRenderer ofxRenderer = new OfxChartRenderer();
//			for (Media media : info.getMedia())
//			{
//
//				Chart chart          = media.getChart();
//				JFreeChart jfreeChart = ofxRenderer.render(chart);
//				BufferedImage chartImage = jfreeChart.createBufferedImage(320, 240);
//				mapReportParameter.put(media.getCode(), chartImage);
//			}		}
		}
		//Add the data document
		doc = JDomUtil.unsetNameSpace(doc);
		Document docReport = JDomUtil.toW3CDocument(doc);
		mapReportParameter.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, docReport);
		ArrayList<JRTemplate> templateList = new ArrayList<JRTemplate>();
		//Add all resources configured in resources.xml
		for (Resource res : resources.getResource())
		{
			logger.info("Adding resource of type " +res.getType() +" with id='" +res.getName() +"' loaded from " +res.getValue().getValue());
			if (res.getType().equals("image"))
				
				{
					BufferedImage image = null;
					try {
						String imgLocation = "/resources/" +res.getType() +"/" +res.getValue().getValue();
						logger.info("Including image resource: " +imgLocation);
						image = ImageIO.read(mrl.searchIs(imgLocation));} 
					catch (FileNotFoundException e) {logger.error(e.getMessage());}
					catch (IOException e) {logger.error(e.getMessage());}
					mapReportParameter.put(res.getName(), image);
				}
				else if (res.getType().equals("vector"))
				{
					String imgLocation = "/resources/" +res.getType() +"/" +res.getValue().getValue();
					
					InputStream vector = null;
				try {
					vector = mrl.searchIs(imgLocation);
				} catch (FileNotFoundException ex) {
					logger.error("Could not load SVG from classpath " +ex.getMessage());
				}
					
					
					logger.info("Including vector resource as File object: " +imgLocation);
					//vector = new File(getClass().getClassLoader().getResource(imgLocation).getFile());
					mapReportParameter.put(res.getName(), vector);
				}
				else if (res.getType().equals("template"))           
				{
                                    
					try {
						String templateLocation = "/resources/templates" +"/" +res.getValue().getValue();
						logger.info("Including style template resource: " +templateLocation);
						JRTemplate style = (JRTemplate) JRXmlTemplateLoader.load(mrl.searchIs(templateLocation));
                                                mapReportParameter.put(res.getName() +"-style", style);
                                                templateList.add(style);
                                        }
					catch (FileNotFoundException e) {logger.error(e.getMessage());}
					catch (IOException e) {logger.error(e.getMessage());}
					
				}
		}
                mapReportParameter.put("REPORT_TEMPLATES", templateList);
		for (Object key : mapReportParameter.keySet())
		{
			String keyString = (String) key;
			logger.info("Key " +keyString);
			String valueString = mapReportParameter.get(keyString).getClass().getCanonicalName();
			logger.info("Report Parameter: " +keyString +" = " +valueString);
		}
		return mapReportParameter;
	}
	

	/*
	 * Get a Map of all standard parameters plus the given locale and the included data XML file
	 * @param reportId
	 * @param format
	 * @return
	 * @throws ReportException
	 */
	public Map<String,Object> getSubreportsMap(String reportId, String format) throws ReportException
	{	
		Map<String,Object> mapReportParameter = new HashMap<String,Object>();
		
		ArrayList<Jr> results = ReportXpath.getSubreports(reports, reportId, format);
		for (Jr jr : results)
		{
			JasperReport jreport = getCompiledReport(reportId, jr.getName(), format, "sr");
			mapReportParameter.put("sr" +jr.getName(), jreport);
		}
		return mapReportParameter;
	}
	
	/*
	 * Get the JasperPrint object for a given JasperReport along with parameter map
	 * @param report
	 * @param mapReportParameter
	 * @return
	 * @throws ReportException
	 */
	public JasperPrint getJasperPrint(JasperReport report, Map<String,Object> mapReportParameter) throws ReportException
	{
		JasperPrint  print = new JasperPrint();
		try {print  = JasperFillManager.fillReport(report, mapReportParameter);}
		catch (JRException e)
		{
			throw new ReportException("Error when trying to create JasperPrint object from compiled JasperReport with given parameter map: " +e.getMessage());
		}
		return print;
	}
	
	/*
	 * Exports the given JasperPrint to an MS Excel xls sheet as ByteArrayOutputStream
	 * @param jPrint
	 * @return
	 * @throws ReportException
	 */
	private ByteArrayOutputStream exportToXls(JasperPrint jPrint) throws ReportException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try
		{	
			logger.info("Exporting report to Excel Sheet");
			JRXlsExporter exporterXLS = new JRXlsExporter();
			exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jPrint);
			exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, os);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		//	NOT WORKING IN JASPERREPORTS 5.0.1:
		//	exporterXLS.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporterXLS.exportReport();	
		}
		catch (JRException e) 
		{
			throw new ReportException("Error exporting JasperPrint to XLS ByteArrayOutputStream: " +e.getMessage());
		}
		return os;
	}
	
	/*
	 * Exports the given JasperPrint to an PDF format as ByteArrayOutputStream
	 * @param jPrint to be exported
	 * @return
	 * @throws ReportException
	 */
	public ByteArrayOutputStream exportToPdf(JasperPrint jPrint) throws ReportException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{	
			logger.info("Exporting report to PDF");
			JasperExportManager.exportReportToPdfStream(jPrint, baos);
		}
		catch (JRException e)
		{
			throw new ReportException("Error exporting JasperPrint to PDF ByteArrayOutputStream: " +e.getMessage());
		}
		return baos;
	}	
	
	/*
	 * Method encapsulating the classical JasperReports workflow of JasperDesign -> JasperReport -> JasperPrint -> PDF/XLS export
	 * @param reportId identifier of the requested report
	 * @param doc XML data object
	 * @param format output format defined by Format enum (PDF or Excel XLS)
	 * @param locale Locale to be used for report exporting
	 * @return
	 * @throws ReportException
	 */
	@Deprecated
	public ByteArrayOutputStream create(String reportId, Document doc, Format format, Locale locale) throws ReportException
	{
		logger.info("TEST");
	//	JasperDesign masterDesign = getMasterReport(reportId, format.name());
	//	JasperReport masterReport = getCompiledReport(masterDesign);
		JasperReport masterReport = getCompiledReport(reportId, null, format.name(), "mr");
		Map<String, Object> reportParameterMap = getParameterMap(doc, locale);
		reportParameterMap.putAll(getSubreportsMap(reportId, format.name()));
		JasperPrint print = getJasperPrint(masterReport, reportParameterMap);
		return exportToPdf(print);
	}
	
	/*
	 * Method adding the usage of JRConcurrentSwapFile to report generation. Avoids OoM exceptions on large reports but may slow down generation.
	 *
	 */
	public JRSwapFileVirtualizer useSwapFileVirtualizer()
	{
		JRConcurrentSwapFile jrSwapFile = new JRConcurrentSwapFile(System.getProperty("java.io.tmpdir"), 30, 4);
		return new JRSwapFileVirtualizer(4, jrSwapFile, true);
	}

	public ByteArrayOutputStream createUsingJDom(String reportId, org.jdom2.Document doc, Format format, Locale locale) throws ReportException
	{
		return createUsingJDom(reportId,doc,format,locale,false); 
	}
	
	/*
	 * Method encapsulating the classical JasperReports workflow of JasperDesign -> JasperReport -> JasperPrint -> PDF/XLS export
	 * @param reportId identifier of the requested report
	 * @param doc XML data object
	 * @param format output format defined by Format enum (PDF or Excel XLS)
	 * @param locale Locale to be used for report exporting
	 * @return
	 * @throws ReportException
	 */
	public ByteArrayOutputStream createUsingJDom(String reportId, org.jdom2.Document doc, Format format, Locale locale, boolean swap) throws ReportException
	{
		logger.info("Using JDom data document.");
		//	JasperDesign masterDesign = getMasterReport(reportId, format.name());
		//	JasperReport masterReport = getCompiledReport(masterDesign);
		JasperReport masterReport = getCompiledReport(reportId, null, format.name(), "mr");
		Map<String, Object> reportParameterMap = getParameterMapJDom(doc, locale);
		reportParameterMap.putAll(getSubreportsMap(reportId, format.name()));
		if(swap) { reportParameterMap.put(JRParameter.REPORT_VIRTUALIZER, useSwapFileVirtualizer());}

		JasperPrint print = getJasperPrint(masterReport, reportParameterMap);

		if(swap){JRVirtualizationContext.getRegistered(print).setReadOnly(true);}

		return exportToPdf(print);
	}
	
	public Reports getReports()
	{
		return reports;
	}
}