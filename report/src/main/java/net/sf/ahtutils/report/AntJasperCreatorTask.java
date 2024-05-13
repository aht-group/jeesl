package net.sf.ahtutils.report;

import java.io.FileNotFoundException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.jeesl.processor.io.report.jasper.ReportJasperCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntJasperCreatorTask extends Task
{
	final static Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	private String configFile, reportRoot, targetDir;
	
    public void execute() throws BuildException
    {
    	try
    	{
			ReportJasperCompiler.instance().compile(configFile, reportRoot, targetDir);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
  	}

	public static void main(String[] args) {
		AntJasperCreatorTask task = new AntJasperCreatorTask();
		task.execute();

	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}


	public String getReportRoot() {
		return reportRoot;
	}

	public void setReportRoot(String reportRoot) {
		this.reportRoot = reportRoot;
	}
}
