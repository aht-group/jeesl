package net.sf.ahtutils.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

import org.jdom2.JDOMException;
import org.jeesl.model.xml.io.report.Jr;
import org.jeesl.model.xml.io.report.Media;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportCompiler
{
	final static Logger logger = LoggerFactory.getLogger(ReportController.class);
	
    static String currentHash;
    
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
	   
    	
    	
		   try {
			JasperCompileManager.compileReportToFile(jrxml, jasper);
		} catch (JRException e) {
			e.printStackTrace();
		}
	   
    	
	 
    	return compilation;
    }
    
    public static Boolean compileReport(InputStream report ,String jrxml, String jasper)
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
    
    public static int[] execute(String configFile, String reportRoot, String targetDir) throws FileNotFoundException
    {
    	logger.info("Using " +configFile +" for report configuration");
    	Reports reports = null;
 		reports = JaxbUtil.loadJAXB(configFile, Reports.class);
 		return execute(reports, reportRoot, targetDir);
    }
    
    public static int[] execute(Reports reports, String reportRoot, String targetDir) 
    {
    	int[] compiledCounter = {0,0,0};
    	 		
 		
 		//Compiling reports
 		logger.info("Pre-Compiling "+reports.getReport().size()+" Report(s)");
 		for(Report report : reports.getReport())
 		{
 			compiledCounter[0]++;
 			logger.info("Report: "+report.getId() +" (" +report.getMedia().size() + " media outputs)");
 			for (Media media : report.getMedia())
 			{
				if (media.getType().equals("pdf"))
				{
					compiledCounter[1]++;
					logger.info("Compiling " +media.getJr().size() +" reports for output -" +media.getType() +"-");
					for (Jr jr : media.getJr())
					{
						compiledCounter[2]++;
						logger.info(jr.getName());
						//Compiling for left to right and right to left languages
						String jrxml  = reportRoot +"/" +"jrxml"  +"/" +report.getDir() +"/" + media.getType() + "/" + jr.getType() + jr.getName() +".jrxml";
						if(report.isSetLtr() && report.isLtr())
						{
							String jasperLtr = targetDir +"/" +"jasper" +"/" +report.getDir() +"/" + media.getType() + "/ltr/" + jr.getType() + jr.getName() +".jasper";
							logger.info("Compiling LTR " +jrxml +" to " +jasperLtr);
							new File(targetDir +"/" +"jasper"  +"/" +report.getDir() +"/" + media.getType() + "/ltr/").mkdirs();
							compileReport(jrxml, jasperLtr);
						}

						if(report.isSetRtl() && report.isRtl())
						{	
							String jasperRtl = targetDir +"/" +"jasper" +"/" +report.getDir() +"/" + media.getType() + "/rtl/" + jr.getType() + jr.getName() +".jasper";
							logger.info("Compiling RTL " +jrxml +" to " +jasperRtl);

							new File(targetDir +"/" +"jasper"  +"/" +report.getDir() +"/" + media.getType() + "/rtl/").mkdirs();
							InputStream in = null;
							try {
								in = ReportUtilRtl.LeftToRightConversion(jrxml);
							} catch (JDOMException e) {
								logger.error("Problem converting to right-to-left language.");
							}
							compileReport(in, jrxml, jasperRtl);
						}
					}
				}
 				
 			}
 		}
 		return compiledCounter;
 	}
}
