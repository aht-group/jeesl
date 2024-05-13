package org.jeesl.processor.io.report.jasper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.exlp.util.jx.JaxbUtil;
import org.jdom2.JDOMException;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.model.xml.io.report.Jr;
import org.jeesl.model.xml.io.report.Media;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.report.ReportController;
import net.sf.ahtutils.report.ReportUtilRtl;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

public class ReportJasperCompiler
{
	final static Logger logger = LoggerFactory.getLogger(ReportController.class);
	
    public static ReportJasperCompiler instance() {return new ReportJasperCompiler();}
    public ReportJasperCompiler()
    {
    	
    }
    
    public int[] compile(String configFile, String reportRoot, String targetDir) throws FileNotFoundException
    {
    	logger.info("Using " +configFile +" for report configuration");
    	Reports reports = null;
 		reports = JaxbUtil.loadJAXB(configFile, Reports.class);
 		return execute(reports,Paths.get(reportRoot,"jrxml"), Paths.get(targetDir).resolve("jasper"));
    }
    
    public int[] compile(String configFile, String reportRoot, Path pTarget) throws FileNotFoundException
    {
    	logger.info("Using " +configFile +" for report configuration");
    	Reports reports = null;
 		reports = JaxbUtil.loadJAXB(configFile, Reports.class);
 		return execute(reports, Paths.get(reportRoot,"jrxml"), pTarget);
    }
    
    private int[] execute(Reports reports, Path pSrcJrxml, Path pDstJasper) 
    {
    	int[] compiledCounter = {0,0,0};
    	 		
 		//Compiling reports
 		logger.info("Pre-Compiling "+reports.getReport().size()+" Report(s)");
 		for(Report report : reports.getReport())
 		{
 			compiledCounter[0]++;
// 			logger.info("Report: "+report.getId() +" (" +report.getMedia().size() + " media outputs)");
 			for (Media media : report.getMedia())
 			{
				if(media.getType().equals("pdf"))
				{
					compiledCounter[1]++;
//					logger.info(report.getId()+": Compiling " +media.getJr().size() +" reports for output -" +media.getType() +"-");
					for (Jr jr : media.getJr())
					{
						compiledCounter[2]++;
						//Compiling for left to right and right to left languages
						Path pJrxml = Paths.get(report.getDir(),media.getType(),jr.getType()+jr.getName()+".jrxml");
						
						if(BooleanComparator.active(report.isLtr()))
						{
							Path pJasper = Paths.get(report.getDir(),media.getType(),"ltr", jr.getType()+jr.getName()+".jasper");
							
							logger.info(report.getId()+": Compiling LTR " +pJrxml.toString() +" to " +pJasper);
							new File(pDstJasper.toString()  +"/" +report.getDir() +"/" + media.getType() + "/ltr/").mkdirs();
							
//							ReportJasperCompiler.compileReport(pJrxml.toString(), jasperLtr);
							
							try {JasperCompileManager.compileReportToFile(pSrcJrxml.resolve(pJrxml).toString(), pDstJasper.resolve(pJasper).toString());}
					    	catch (JRException e) {e.printStackTrace();}
						}

						if(BooleanComparator.active(report.isRtl()))
						{	
							String jasperRtl = pDstJasper.toString() +"/" +report.getDir() +"/" + media.getType() + "/rtl/" + jr.getType() + jr.getName() +".jasper";
							logger.info("Compiling RTL " +pJrxml.toString() +" to " +jasperRtl);

							new File(pDstJasper.toString()  +"/" +report.getDir() +"/" + media.getType() + "/rtl/").mkdirs();
							try
							{
								InputStream in = ReportUtilRtl.LeftToRightConversion(pJrxml.toString());
								compileReport(in, pJrxml.toString(), jasperRtl);
							}
							catch (JDOMException e) {logger.error("Problem converting to right-to-left language.");}
						}
					}
				}
 				
 			}
 		}
 		return compiledCounter;
 	}
    
    public static Boolean compileReport(String jrxml, String jasper)
    {
    	Boolean compilation = true;
    	
//    	ReportUtilHash hashUtil = new ReportUtilHash(jrxml);
//    	String oldHash = hashUtil.readAndRemoveHash();
//    	String newHash = hashUtil.calculateHash();
//    	log.add("Hash was:");
//    	log.add(oldHash);
//    	log.add(newHash);
//    	log.add("is new Hash");
//    	
//    	if (!(newHash.equals(oldHash)))
//    	{
//    		compilation=true;
//    		log.add("Re-Compilation needed.");
//    	}
//		else
//		{
//			compilation = false;
//		    log.add("Re-Compilation not needed.");
//		}
//    	
//    	   if (compilation || !(new File(jasper).exists()))
//   	    {
//   	    	try {
//   				JasperCompileManager.compileReportToFile(jrxml, jasper);
//   				hashUtil.saveNewHash();
//   			} catch (JRException e) {
//   				logger.error(e.getMessage());
//   				log.add(e.getMessage());
//   			}
//   			logger.info("Compiling " +jrxml +" to " +jasper);
//   			log.add("Compiled  " +jrxml +" to " +jasper);
//   	    }
	   
    	
    	
    	try {JasperCompileManager.compileReportToFile(jrxml, jasper);}
    	catch (JRException e) {e.printStackTrace();}
	   
    	return compilation;
    }
    
    public static Boolean compileReport(InputStream report, String jrxml, String jasper)
    {
    	Boolean compilation = true;
    	
//    	ReportUtilHash hashUtil = new ReportUtilHash(report, jrxml);
//    	String oldHash = hashUtil.readAndRemoveHash();
//    	String newHash = hashUtil.calculateHash();
//    	log.add("Hash was:");
//    	log.add(oldHash);
//    	log.add(newHash);
//    	log.add("is new Hash");
//    	
//    	if (!(newHash.equals(oldHash)))
//    	{
//    		compilation=true;
//    		log.add("Re-Compilation needed.");
//    	}
//		else
//		{
//			compilation = false;
//		    log.add("Re-Compilation not needed.");
//		}
//    	
//    	
//	    if (compilation || !(new File(jasper).exists()))
//	    {
//	    	try {
//				JasperCompileManager.compileReportToFile(jrxml, jasper);
//				hashUtil.saveNewHash();
//			} catch (JRException e) {
//				logger.error(e.getMessage());
//				log.add(e.getMessage());
//			}
//			logger.info("Compiling " +jrxml +" to " +jasper);
//			log.add("Compiled  " +jrxml +" to " +jasper);
//	    }
    	
	    try {JasperCompileManager.compileReportToFile(jrxml, jasper);}
	    catch (JRException e) {logger.error(e.getMessage());}
	    logger.debug("Target File was " +jasper);
    	return compilation;
    }
}