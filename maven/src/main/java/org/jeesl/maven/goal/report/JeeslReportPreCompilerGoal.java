package org.jeesl.maven.goal.report;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;

import net.sf.ahtutils.report.ReportCompiler;

/**
 * Goal which compiles a set of JasperReports jrxml files to .jasper file. Creates a rtl language and a ltr language version of all reports.
 */
@Mojo(name ="compileReports", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class JeeslReportPreCompilerGoal extends AbstractMojo
{
	@Parameter(defaultValue = "${project.basedir}/src/main/resources/reports.${project.artifactId}/reports.xml", required = true)
    private String configFile;
    
    @Parameter(defaultValue = "${project.basedir}/src/main/reports.${project.artifactId}", required = true)
    private String source;
    
    @Parameter(defaultValue = "${project.build.directory}/classes/reports.${project.artifactId}", required = true)
    private String target;
    
    @Parameter(defaultValue = "WARN")
    private String log;
	
    public void execute() throws MojoExecutionException
    {
    	BasicConfigurator.configure();
    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel(log));
    	
    	getLog().info("Using configFile " +configFile);
    	getLog().info("Using jrxml from " +source);
    	getLog().info("Compiling jasper to " +target);
    	
    	ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();
    	int[] counter;
		try
		{
			counter = ReportCompiler.execute(configFile, source, target);
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