package net.sf.ahtutils.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.jdom2.JDOMException;
import org.jeesl.model.xml.io.report.Jr;
import org.jeesl.model.xml.io.report.Media;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractReportControl
{
	final static Logger logger = LoggerFactory.getLogger(AbstractReportControl.class);

	public static enum Output{pdf,xls}
	public static enum Source{database,example,report}
	public static enum Direction{ltr,rtl}	
	
	protected Reports config;
	protected MultiResourceLoader mrl;
	protected Map<String,Object> mapReportParameter;
	
	protected String rId,lang;
	protected Output output;
	protected Direction direction;
	protected JasperPrint jPrint;
	
	protected String reportRoot = "";
	
	public AbstractReportControl(Reports config)
	{
		this.config=config;
		this.reportRoot = config.getDir();
		mrl = new MultiResourceLoader();
		mapReportParameter = new Hashtable<String,Object>();
		jPrint = new JasperPrint();
		lang = "en";
	}
	
	public void setParameter(String lang, String rId)
	{
		this.lang=lang;
		this.rId=rId;
	}
	
	@Deprecated
	public void setReportRoot(String reportRoot)
	{
		this.reportRoot = reportRoot;
	}
	
	public JasperPrint getJasperPrint(Output output, Direction dir)
	{
		JasperPrint  print = new JasperPrint();
		JasperReport report = getMasterReport(output,dir);
		compileSubReports(output, dir);
		try {print  = JasperFillManager.fillReport(report, mapReportParameter);}
		catch (JRException e) {e.printStackTrace();}
		return print;
	}
	
	
	
	public void compileSubReports(Output output, Direction dir)
	{	
		Report  report  = (Report)JXPathContext.newContext(config).getValue("report[id='" +rId +"']");
		for (Media media : report.getMedia())
		{
			logger.info("Found reports for " +media.getType() +": " +media.getJr().size());
			String rDir = reportRoot +"/jrxml/"+report.getDir() +"/" +media.getType() +"/";
			for (Jr jr : media.getJr())
			{
				if (jr.getType().equals("sr"))
				{
					String fileName = jr.getType() +jr.getName();
					logger.info("Compiling " +fileName);
					JasperReport jreport = getReport(rDir, fileName,dir);
					mapReportParameter.put(fileName, jreport);
				}
			}
		}
	}
	
	protected JasperReport getMasterReport(Output output, Direction dir)
	{
		JasperReport report = null;
		Jr jr  = (Jr)JXPathContext.newContext(config).getValue("report[id='"+ rId +"']/media[type='" +output.name() +"']/jr[type='mr']");
		Report rep = (Report)JXPathContext.newContext(config).getValue("report[id='"+ rId +"']");
		String rName = jr.getName();
		String rDir  = reportRoot +"/jrxml/"+(String)JXPathContext.newContext(config).getValue("report[id='" +rId +"']/@dir") +"/" +output.name();
		String reportDir = reportRoot.replace("src/main/resources/", "");
		try
		{
			report = (JasperReport) JRLoader.loadObject(mrl.searchIs(reportDir +"/jasper/" +rep.getDir()+"/" +output.name() +"/" +dir.name() +"/mr" +rName +".jasper"));
		}
		catch (FileNotFoundException e)
		{
			logger.warn("File Not Found - compiling Report!");
			report = getReport(rDir, "mr"+rName,dir);
		}
		catch (JRException e) {e.printStackTrace();}
		return report;
		//return getReport(rDir, "mr"+rName,dir);
	}
	
	private JasperReport getReport(String rDir, String fileName, Direction dir)
	{
		JasperReport report = null;
		fileName = rDir+"/"+fileName;
		fileName = FilenameUtils.normalize(fileName);
		fileName = fileName.replace("\\", "/");
		File compiledReport = new File(fileName+".jasper");
		if (compiledReport.exists())
		{
			logger.info("Loading pre-compiled Report: "+fileName);
			FileInputStream in;
			try {
				in = new FileInputStream(compiledReport);
				report = (JasperReport)JRLoader.loadObject(in);
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
			} catch (JRException e) {
				logger.error(e.getMessage());
			} 
		}
		else
		{
			fileName = fileName +".jrxml";
			logger.info("Compiling Report: "+fileName);
			try
			{
				InputStream is=null;
				switch(dir)
				{
					case ltr: is = mrl.searchIs(fileName);break;
					case rtl: is = ReportUtilRtl.LeftToRightConversion(fileName);break;
				}
				report = JasperCompileManager.compileReport(is);
			}
			catch (JRException e) {logger.error("",e);}
			catch (FileNotFoundException e) {logger.error("",e);}
			catch (JDOMException e) {logger.error("",e);}
		}
		return report;
	}
}