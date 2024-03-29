package org.jeesl.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.interfaces.io.NsPrefixMapperInterface;
import org.exlp.util.io.log.LoggerInit;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.interfaces.controller.report.format.JeeslXlsReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.model.xml.io.report.Info;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.jeesl.model.xml.io.report.Resources;
import org.jeesl.util.query.xpath.ReportXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import net.sf.ahtutils.report.ReportHandler;
import net.sf.ahtutils.report.exception.ReportException;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JDomUtil;

public class AbstractJeeslReportTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslReportTest.class);
	
	protected static NsPrefixMapperInterface nsPrefixMapper;
	protected File f;
	protected boolean saveReference=false;

	protected static File fTarget;
	
	protected static void setfTarget(File myTarget) {fTarget=myTarget;}

	protected static String reportRoot;
	protected static String configFile;
    protected static String resourcesFile;
    protected static String reportId;
    
    protected static Reports reports;
    protected static Resources resources;
    
    protected Document docReport;
    protected org.jdom2.Document jdomReport;
    protected Report report;
    protected ReportHandler reportHandler;
    protected ByteArrayOutputStream pdf;
    protected static String reportFileLocation;
    protected static String loggerConfigFile;
    protected static String loggerConfigPath;
	
    public AbstractJeeslReportTest() {}
    
    @BeforeClass
	public static void initTargetDirectory()
	{
		if(fTarget==null)
		{
			String dirTarget = System.getProperty("targetDir");
			if(dirTarget==null){dirTarget="target";}
			setfTarget(new File(dirTarget));
			if(LoggerInit.isLog4jInited())
			{
				logger.debug("Using targeDir "+fTarget.getAbsolutePath());
			}
		}
	}
    
	public static void initFile()
	{
		if(!LoggerInit.isLog4jInited()){initLogger();}
		String dirTarget = System.getProperty("targetDir");
		if(dirTarget==null){dirTarget="target";}
		setfTarget(new File(dirTarget));
		logger.debug("Using targeDir "+fTarget.getAbsolutePath());
	}
	
	public static void initLogger()
	{
		if(!LoggerBootstrap.isLog4jInited())
		{
			LoggerBootstrap.instance().path("jeesl/system/io/log").init();
		}
    }
	
	@Before
	public void initHandler() throws ReportException, FileNotFoundException
	{
		reportHandler = new ReportHandler(reportFileLocation);
		reports       = reportHandler.getReports();
	}
	
	protected void initExample(String id) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		reportId = id;
		report = ReportXpath.getReport(reports, id);
		logger.info("Loading Report "+report.getExample());
		
		//Load the JDom representation of the example for further processing in ReportHandler
		jdomReport = JDomUtil.load(report.getExample());
		
		//		logger.info("Reading XML demo data from:" +report.getExample());
		//		JDomUtil.debug(jdomDoc);
		docReport = JDomUtil.toW3CDocument(JDomUtil.unsetNameSpace(jdomReport));
	}
	
	protected void createPdf() throws ReportException
	{
		pdf = reportHandler.createUsingJDom(reportId, this.jdomReport, ReportHandler.Format.pdf, Locale.GERMAN,false);
	}
	
	protected void writePdf() throws IOException
	{
		Info info = ReportXpath.getReportInfo(jdomReport);
		
		StringBuffer sb = new StringBuffer();
		sb.append("target/");
		if(Objects.nonNull(info) && Objects.nonNull(info.getFile()) && info.getFile().getValue().length()>0)
		{
			// Replacing the /'s is required, because they may be interpreted as directory separators
			sb.append(info.getFile().getValue().replaceAll("/", "-"));
		}
		else
		{
			sb.append(reportId);
		}
		sb.append(".pdf");
		System.out.println("Writing PDF demo to " +sb.toString());
		OutputStream outputStream = new FileOutputStream (sb.toString());
		pdf.writeTo(outputStream);
	}
	
	protected void writePdf(String filename) throws IOException
	{
		OutputStream outputStream = new FileOutputStream (filename);
		pdf.writeTo(outputStream);
	}
	
	protected void assertEmptyPage(byte[] data) throws IOException
	{
        PdfReader reader = new PdfReader(data);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        TextExtractionStrategy strategy = parser.processContent(1, new SimpleTextExtractionStrategy());
        reader.close();
		strategy.getResultantText();
		Assert.assertTrue("First generated page counts zero characters",strategy.getResultantText().length()>0);
	}
	
	protected void assertJaxbEquals(Object expected, Object actual)
	{
		Assert.assertEquals("actual XML differes from expected XML",JaxbUtil.toString(expected),JaxbUtil.toString(actual));
	}
	
	protected <REPORT extends JeeslIoReport<?,?,?,?>> void outputXls(JeeslXlsReport<REPORT> report) throws Exception
    {
		File fDst = new File(fTarget,report.xlsFileName());
		logger.debug("Saving to "+fDst);
    	IOUtils.copy(report.xlsStream(), new FileOutputStream(fDst));
    }
}