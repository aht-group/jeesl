package org.jeesl.maven.goal;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.exlp.model.xml.io.Dir;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.txt.system.locale.TranslationFactory;

import net.sf.exlp.util.xml.JaxbUtil;

@Mojo(name="msgBundle2")
public class JeeslMsgGoal extends AbstractMojo
{
	@Parameter(defaultValue="WARN")
    private String log;
    
	@Parameter(defaultValue="${project.groupId}")
    private String groupId;
	
	@Parameter(defaultValue="${project.parent.artifactId}")
    private String projectArtifactId;
	
	@Parameter(defaultValue="${project.artifactId}")
    private String artifactId;
	
	@Parameter(defaultValue="${basedir}/../doc/src/main/resources/msg.${project.parent.artifactId}")
    private String msgSource;
	
	@Parameter(defaultValue="${project.build.directory}")
    private String projectBuildDirectory;
	
	@Parameter(defaultValue="${basedir}/src/main/resources/msg.${project.artifactId}")
    private String targetDir;
	
	@Parameter(defaultValue="translation.xml")
    private String translationXml;
	
	@Parameter(defaultValue="msg.${project.artifactId}")
    private String prefixXml;
    	
    public void execute() throws MojoExecutionException
    {
    	BasicConfigurator.configure();
    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel(log));
	
    	getLog().info("groupId: "+groupId);
    	getLog().info("projectArtifactId: "+projectArtifactId);
    	getLog().info("artifactId: "+artifactId);
    	getLog().info("msgSource: "+msgSource);
    	getLog().info("projectBuildDirectory: "+projectBuildDirectory);
    	getLog().info("targetDir: "+targetDir);
    	getLog().info("translationXml: ?? "+translationXml);
    	
    	File fTarget = JeeslMsgGoal.createTargetDir(targetDir);
    	
    	File fRoot = new File(msgSource);
    	if(!fRoot.exists()){throw new MojoExecutionException("msg.bundle directory does not exist: "+fRoot.getAbsolutePath());}
    	
    	File fTranslationsXml = new File(fTarget,translationXml);
    	
    	getLog().info("Creating MessageBundle "+groupId+".msg_<lang>.txt from "+msgSource);
    	
    	TranslationFactory tFactory = new TranslationFactory();
		tFactory.setOutEncoding("UTF-8");
		try
		{
			Dir dir = tFactory.rekursiveDirectory(fRoot.getAbsolutePath());
			dir.setName(prefixXml);
			getLog().info("Saving XML summary file to"+fTranslationsXml.getAbsolutePath());
			JaxbUtil.save(fTranslationsXml,dir,true);
			
			tFactory.writeMessageResourceBundles("msg",fTarget);
			for(String s : tFactory.getStats())
			{
				getLog().info(s);
			}
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
    }
    
    public static File createTargetDir(String td) throws MojoExecutionException
    {
	    	File fTarget = new File(td);
	    	
	    	if(!fTarget.exists())
	    	{
	    		if(!fTarget.getParentFile().exists()){throw new MojoExecutionException("Parent directory (of target) does not exist: "+fTarget.getParentFile().getAbsolutePath());}
	    		else if(!fTarget.getParentFile().isDirectory()){throw new MojoExecutionException("Parent (of target) is not a directory: "+fTarget.getParentFile().getAbsolutePath());}
	    		else
	    		{
	    			fTarget.mkdir();
	    		}
	    	}
	    	else if(!fTarget.isDirectory())
	    	{
	    		throw new MojoExecutionException("Target is not a directory: "+fTarget.getAbsolutePath());
	    	}
	    	
	    	return fTarget;
    }
}