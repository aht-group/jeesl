package org.jeesl.maven.goal.report;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Objects;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.processor.io.report.jasper.ReportJasperCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Goal which compiles a set of JasperReports jrxml files to .jasper file. Creates a rtl language and a ltr language version of all reports.
 */
@Mojo(name ="compileReports", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class JeeslReportPreCompilerGoal extends AbstractMojo
{
	final static Logger logger = LoggerFactory.getLogger(JeeslReportPreCompilerGoal.class);
	
	@Parameter(defaultValue = "${project.basedir}/src/main/resources/reports.${project.artifactId}/reports.xml", required = true)
    private String configFile;
    
    @Parameter(defaultValue = "${project.basedir}/src/main/reports.${project.artifactId}", required = true)
    private String source;
    
    @Parameter(defaultValue = "${project.build.directory}/classes/reports.${project.artifactId}", required = true)
    private String target;
    
    @Parameter(required=false)
    private String target2;
    
    @Parameter(defaultValue = "WARN")
    private String log;
	
    public void execute() throws MojoExecutionException
    {
    	Path p2 = Paths.get(target2);
    	
    	logger.info("Config File: "+configFile);
    	logger.info("Source jrxml: " +source);
    	logger.info("Compiling (v1).jasper-files to " +target);
    	logger.info("Compiling (v2).jasper-files to " +target2);
    	logger.info("Compiling (v2).jasper-files to path " +p2.toString());
    	
    	ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();
    	int[] counter;
		try
		{
			ReportJasperCompiler jasper = new ReportJasperCompiler();
			
			counter = jasper.compile(configFile, source, target);
			
			if(Objects.nonNull(target2))
			{
				logger.info("t2: "+target2);
				jasper.compile(configFile, source, p2);
			}

		}
		catch (FileNotFoundException e) {throw new MojoExecutionException("Report file not found.");}
    	
    	
    	DecimalFormat df = new DecimalFormat("#00");
    	df.setDecimalSeparatorAlwaysShown(false);
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("Compiled ").append(counter[0]).append(" reports");
    	sb.append(" (media:"+counter[1]+" jr:"+counter[2]+")");
    	sb.append(" in ").append(ptt.toTotalPeriod());
    	    	
    	getLog().info(sb.toString());
    }
}