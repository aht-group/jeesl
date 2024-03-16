package org.jeesl.maven.goal.js;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.factory.js.JsFactory;
import net.sf.exlp.util.io.RelativePathFactory;
import net.sf.exlp.util.io.RelativePathFactory.PathSeparator;

@Mojo(name="createJs")
public class JsBuilderGoal extends AbstractMojo
{
	final static Logger logger = LoggerFactory.getLogger(JsBuilderGoal.class);
	
	@Parameter(defaultValue="WARN")
    private String log;
    
	@Parameter(defaultValue="${project.basedir}")
    private String projectBaseDir;
    
	@Parameter(defaultValue="${project.build.directory}/classes/META-INF/resources/ahtutilsCss")
    private String jsDir;
    
	@Parameter(defaultValue="${project.build.directory}/noName.jsf")
    private String targetFile;
    
	@Parameter
    private String[] libOrder;
    
    
    public void execute() throws MojoExecutionException
    {    	
    	File fProject = new File(projectBaseDir);
    	if(!fProject.exists()){throw new MojoExecutionException(fProject.getAbsolutePath()+" does not exist");}
    	if(!fProject.isDirectory()){throw new MojoExecutionException(fProject.getAbsolutePath()+" is not a directory");}
    	
    	File fJsDir = new File(fProject,jsDir);
    	if(!fJsDir.exists()){throw new MojoExecutionException(fJsDir.getAbsolutePath()+" does not exist");}
    	if(!fJsDir.isDirectory()){throw new MojoExecutionException(fJsDir.getAbsolutePath()+" is not a directory");}
    	   	
    	RelativePathFactory rpf = new RelativePathFactory(fProject,PathSeparator.CURRENT);
    	getLog().info("JS Directory: "+rpf.relativate(fJsDir));
    	
    	File fTarget = new File(targetFile);
    	if(!fTarget.getParentFile().exists()){throw new MojoExecutionException(fTarget.getParentFile().getAbsolutePath()+" does not exist");}
    	if(!fTarget.getParentFile().isDirectory()){throw new MojoExecutionException(fTarget.getParentFile().getAbsolutePath()+" is not a directory");}
    	if(fTarget.isDirectory()){throw new MojoExecutionException(fTarget.getAbsolutePath()+" is a directory");}
    	if(fTarget.exists()){fTarget.delete();}
    	
    	getLog().info("Target: "+rpf.relativate(fTarget));
    	
		try
		{
			JsFactory jsFactory = new JsFactory(fJsDir, libOrder);
			jsFactory.write(fTarget);
		}
		catch (EvaluatorException e) {throw new MojoExecutionException(e.getMessage());}
		catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
}