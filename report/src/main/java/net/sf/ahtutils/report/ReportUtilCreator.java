package net.sf.ahtutils.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.apache.commons.io.IOUtils;
import org.exlp.util.jx.JaxbUtil;
import org.exlp.util.system.DateUtil;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jeesl.model.xml.io.report.Jr;
import org.jeesl.model.xml.io.report.Media;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.jeesl.model.xml.io.report.Templates;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ReportUtilCreator
{
	
	private Logger logger = LoggerFactory.getLogger(ReportUtilCreator.class);
	
	private String configFile;
	private String templateFile;
	private String jrxmlDir;
    private String resourcesFile;
    private String reportId;
    private String testPackage;
    private Boolean productive;
    private String abstractTestclass;
    
    private MultiResourceLoader mrl = MultiResourceLoader.instance();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute() throws JRException, TemplateException, IOException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	
		logger.info("Using " +configFile +" for report configuration.");
		logger.warn("Running in TEST mode - look for generated files in target directory. To create files in project directories use setProductive(true).");
        Reports reports;
		reports = (Reports)JaxbUtil.loadJAXB(configFile, Reports.class);
		
		ReportUtilTemplate templateManager = null;
		templateManager = new ReportUtilTemplate(reports.getDir());
		
		Report report = new Report();
		
        logger.info("Adding " +reportId +" report to system.");
        Boolean existing = true;
        try {
			ReportXpath.getReport(reports, reportId);
			report = ReportXpath.getReport(reports, reportId);
		} catch (ExlpXpathNotFoundException e) {
			existing = false;
		} catch (ExlpXpathNotUniqueException e) {
			logger.error(e.getMessage());
		}
        if (!existing)
        {
        	report.setId(reportId);
    		report.setLtr(true);
    		report.setDir(reportId);
    		report.setExample("src/test/resources/data/xml/report/" +reportId +".xml");
    		
    		Media pdf = new Media();
    		pdf.setType("pdf");
    		Jr jasperReport = new Jr();
    		jasperReport.setName(reportId);
    		jasperReport.setType("mr");
    		
    		pdf.getJr().add(jasperReport);
    		report.getMedia().add(pdf);
    		
    		reports.getReport().add(report);
    		if (productive)
    		{
    			JaxbUtil.save(new File(configFile), reports, true);
    		}
    		else
    		{
    			JaxbUtil.save(new File("target/reports.xml"), reports, true);
    		}
    		logger.info("Reports.xml updated.");
        }
		
		String reportFilename = reports.getDir() +"/" +reportId +"/" +"mr" +reportId +".jrxml";
		
		//Load the template given by template name
		Templates templates = JaxbUtil.loadJAXB(templateFile, Templates.class);
		org.jeesl.model.xml.io.report.Template template = ReportXpath.getTemplate(templates, getReportId());
		
		
		JasperDesign design = templateManager.create(template);
		//JRXmlWriter cares about writing the in-memory design to an OutputStream
		if (productive)
		{
			Boolean dirCreated = new File(jrxmlDir +"/" +reportId +"/pdf/").mkdirs();
			logger.info("Creation of new jrxml directory reports: " +dirCreated);
		}
		String jrxmlFilename = jrxmlDir +"/" +reportId +"/pdf/mr" +reportId +".jrxml";
		if (!productive)
		{
			jrxmlFilename = "target/mr" +reportId +".jrxml";
		}
		JRXmlWriter.writeReport(design, jrxmlFilename, "UTF-8");
		logger.info("Basic jrxml report definition " +"written to " +jrxmlFilename);
		
		logger.info("Creating JUnit test.");
		Map datamodel = new HashMap();
		datamodel.put("reportId", reportId);
		datamodel.put("testPackage", testPackage);
		datamodel.put("classname", "Test" +reportId +"Renderer");
		datamodel.put("abstracttest", abstractTestclass.substring(abstractTestclass.lastIndexOf(".")+1));
		datamodel.put("abstracttestimport", abstractTestclass);
		Configuration cfg = new Configuration();
		StringTemplateLoader loader = new StringTemplateLoader();
		loader.putTemplate("template", IOUtils.toString(mrl.searchIs("freemarker/report-test.jva"), "UTF-8"));
		cfg.setTemplateLoader(loader);
		Template tpl = cfg.getTemplate("template");
		
		String testFileName  = "Test" +reportId +"Renderer.java";
		String testDirectory = "src/test/java/" +testPackage.replaceAll("\\.", "/") +"/";
		new File(testDirectory).mkdirs();
		
		File testFile = new File(testDirectory +testFileName);
		
		if (!productive)
		{
			testFile = new File("target/Test" +reportId +"Renderer.java");
		}
		
		logger.info("Writing JUnit test to " +testFile.getAbsolutePath());
		OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(testFile));
		tpl.process(datamodel, output);
		
		logger.info("Creating demo XML.");
		Element demo = new Element("report");
		
		
		Element info = new Element("info");
		Element title = new Element("title");
		title.setText("AHTUtils Demo Report");
		info.addContent(title);
		
		Element subtitle = new Element("subtitle");
		subtitle.setText("generated by ReportUtilCreator class.");
		info.addContent(subtitle);
		
		Element footer = new Element("footer");
		footer.setText("footer text");
		info.addContent(footer);
		
		Element record = new Element("record");
		XMLGregorianCalendar greg = DateUtil.toXmlGc(new Date());
		record.setText(greg.toString());
		info.addContent(record);
		
		demo.addContent(info);
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		if (productive)
		{
			xmlOutput.output(demo, new FileWriter(report.getExample()));
		}
		else
		{
			xmlOutput.output(demo, new FileWriter("target/" +reportId +"DemoData.xml"));
		}
		
        
    }
	
   public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getResourcesFile() {
		return resourcesFile;
	}

	public void setResourcesFile(String resourcesFile) {
		this.resourcesFile = resourcesFile;
	}

	public String getJrxmlDir() {
		return jrxmlDir;
	}

	public void setJrxmlDir(String jrxmlDir) {
		this.jrxmlDir = jrxmlDir;
	}

	public String getTestPackage() {
		return testPackage;
	}

	public void setTestPackage(String testPackage) {
		this.testPackage = testPackage;
	}

	public Boolean getProductive() {
		return productive;
	}

	public void setProductive(Boolean productive) {
		this.productive = productive;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public String getAbstractTestclass() {
		return abstractTestclass;
	}

	public void setAbstractTestclass(String abstractTestclass) {
		this.abstractTestclass = abstractTestclass;
	}

}
