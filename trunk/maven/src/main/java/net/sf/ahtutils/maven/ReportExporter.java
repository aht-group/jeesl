package net.sf.ahtutils.maven;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.ahtutils.xml.report.Jr;
import net.sf.ahtutils.xml.report.Media;
import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import net.sf.exlp.util.xml.DomUtil;
import net.sf.exlp.util.xml.JDomUtil;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;

/**
 * Goal which fills all reports with example data and exports them to output formats.
 *
 * @goal exportReports
 * 
 * @phase process-sources
 */
public class ReportExporter extends AbstractMojo
{
	/**
     * Location of the file.
     * @parameter expression="src/main/resources/reports/reports.xml"
     * @required
     */
    private String configFile;
    
    /**
     * Location of the file.
     * @parameter expression="src/main/resources/reports/resources.xml"
     * @required
     */
    private String resourcesFile;
	
    public void execute() throws MojoExecutionException
    {	
        getLog().info("Using " +configFile +" for report configuration.");
		
		Reports reports;
		try {reports = (Reports)JaxbUtil.loadJAXB(configFile, Reports.class);}
		catch (FileNotFoundException e) {throw new MojoExecutionException(e.getMessage());}
		
		getLog().info("Exporting "+reports.getReport().size()+" Report(s)");
		for(Report report : reports.getReport())
		{
			getLog().info("Report: "+report.getId() +" (" +report.getMedia().size() + " media outputs)");
			String exampleXML = System.getProperty("user.dir") +"/" +report.getExample();
			getLog().info("Loading example data from XML file: " +exampleXML);
			Document document = null;
			
			MultiResourceLoader mrl = new MultiResourceLoader();
			getLog().info("XML available: "+mrl.isAvailable(exampleXML));
			
			try
			{
				document = JRXmlUtils.parse(JRLoader.getLocationInputStream(exampleXML));
			} catch (JRException e1) {e1.printStackTrace();}
			
			DomUtil.debugDocument(document);
			
			org.jdom2.Document jdomDoc =  JDomUtil.load(exampleXML);
			jdomDoc = JDomUtil.unsetNameSpace(jdomDoc);
			
			document =JDomUtil.toW3CDocument(jdomDoc);
			DomUtil.debugDocument(document);
			
//			File file = new File(exampleXML);
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder db;
//			try {
//				db = dbf.newDocumentBuilder();
//				document = db.parse(file);
//				}
//			catch (SAXException e) {e.printStackTrace();}
//			catch (IOException e) {e.printStackTrace();}
//			catch (ParserConfigurationException e1) {e1.printStackTrace();}
			
			getLog().info("Using data: " +document.toString());
			
			
			for (Media media : report.getMedia())
			{
				Map params = new HashMap();
		        Map<Integer, Boolean> renderedSuccessList = new HashMap<Integer, Boolean>();
		        getLog().info("Filling parameter map.");
		        try {
		            params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
		            params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
		            params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
		            params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
		            params.put(JRParameter.REPORT_LOCALE, Locale.US);
		            
		            String masterReport = null;
		            
		            for (Jr jr : media.getJr())
		            {
		            	if (jr.getType().equals("mr"))
		            	{
		            		masterReport = report.getDir() +"/jrxml/" +jr.getType() +jr.getName() +".jasper";
		            	}
		            	else
		            	{
		            		JasperReport subreport = (JasperReport) JRLoader.loadObject(report.getDir() +"/jrxml/" +jr.getType() +jr.getName() +".jasper");
		            		params.put(jr.getType() +jr.getName(), subreport);
		            	}
		            }

		            JasperPrint filledReport = JasperFillManager.fillReport(masterReport, params);
		            getLog().info("Exporting " +report.getId() +" to " +media.getType() +" file.");
		            if (media.getType().equals("pdf"))
		            {
		            	getLog().info("Exporting " +report.getId() +" to " +report.getDir() +"/" +report.getId() + ".pdf");
		            	JasperExportManager.exportReportToPdfFile(filledReport, report.getDir() +"/" +report.getId() + ".pdf");
		            }
		            
		            if (media.getType().equals("xls"))
		            {
		            	ByteArrayOutputStream output = new ByteArrayOutputStream();
		                OutputStream outputfile= new FileOutputStream(new File(report.getDir() +"/" +report.getId() + ".xls"));

		                JRXlsExporter exporterXLS = new JRXlsExporter();
		                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, filledReport);
		                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
		                exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		            //  NOT WORKING IN JASPERREPORTS 5.0.1:
		            //  exporterXLS.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
		                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		                exporterXLS.exportReport();
		                outputfile.write(output.toByteArray()); 
		            } 
		            


		        } catch (Exception e) {
		            getLog().error("Problem exporting report; " +e.getMessage());
		        }

			}
		}
		
		//Filling and exporting reports TODOs list
		
		// 1.) GetReport Jasperfile -check-
		// 2.) GetSubreports Jasperfiles and add them to map
		// 3.) Read resources.xml and add elements to map
		// 4.) Export -check-
		
    }
}
