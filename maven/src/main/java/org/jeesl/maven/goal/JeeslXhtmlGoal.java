package org.jeesl.maven.goal;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jeesl.processor.JeeslXhtmlParser;
import org.xml.sax.SAXException;

@Mojo(name="parseXhtml")
public class JeeslXhtmlGoal extends AbstractMojo
{
	@Parameter(defaultValue = "WARN")
    private String log;

	@Parameter(defaultValue="${project.groupId}")
    private String groupId;

	@Parameter(defaultValue="${project.parent.artifactId}")
    private String projectArtifactId;

	@Parameter(defaultValue="${project.artifactId}")
    private String artifactId;

	@Parameter(defaultValue="${basedir}/src/main/webapp/jsf")
    private String xhtmlSource;

	@Parameter(defaultValue="${project.build.directory}")
    private String projectBuildDirectory;

    @Override
	public void execute() throws MojoExecutionException
    {
    	BasicConfigurator.configure();
    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel(log));

    	getLog().info("groupId: "+groupId);
    	getLog().info("projectArtifactId: "+projectArtifactId);
    	getLog().info("artifactId: "+artifactId);
    	getLog().info("xhtmlSource: "+xhtmlSource);
    	getLog().info("projectBuildDirectory: "+projectBuildDirectory);

    	File fRoot = new File(xhtmlSource);
    	if(!fRoot.exists()){throw new MojoExecutionException("msg.bundle directory does not exist: "+fRoot.getAbsolutePath());}

		try
		{
			JeeslXhtmlParser xhtmlParser = new JeeslXhtmlParser(fRoot.getAbsolutePath());
			xhtmlParser.parse();
			getLog().info("Processed " + xhtmlParser.getCount() +" Xhtml files ");

			for(String s : xhtmlParser.getStats())
			{
				getLog().error(s);
			}
			if(xhtmlParser.getStats().size() > 0)
			{
				throw new MojoExecutionException("xhtml parse excepction: "+fRoot.getAbsolutePath());
			}
		}
		catch (ParserConfigurationException e) {throw new MojoExecutionException(e.getMessage());}
		catch (SAXException e) {throw new MojoExecutionException(e.getMessage());}
    }

}