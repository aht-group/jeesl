package net.sf.ahtutils.test.report;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.ahtutils.report.ReportUtilCreator;
import net.sf.ahtutils.test.AbstractAhtUtilsReportTest;
import net.sf.exlp.util.exception.ExlpXpathNotFoundException;
import net.sf.exlp.util.exception.ExlpXpathNotUniqueException;
import net.sf.jasperreports.engine.JRException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import freemarker.template.TemplateException;


public class TestReportCreator extends AbstractAhtUtilsReportTest
{
	final static Logger logger = LoggerFactory.getLogger(TestReportCreator.class);
    
	@Test
	public void createReport() throws JRException, TemplateException, IOException, ExlpXpathNotFoundException, ExlpXpathNotUniqueException, ParserConfigurationException, SAXException, ClassNotFoundException
	{
		ReportUtilCreator creator = new ReportUtilCreator();
		creator.setConfigFile(reportFileLocation +"/");
		creator.setTemplateFile("src/main/resources/reports.ahtutils-report/templates.xml");
		creator.setResourcesFile("src/main/resources/reports.ahtutils-report/resources.xml");
		creator.setJrxmlDir("src/main/resources/reports.ahtutils-report/jrxml");
		creator.setReportId("sectest");
		creator.setTestPackage("net.sf.ahtutils.test.report");
		creator.setProductive(true);
		creator.execute();
	}
}