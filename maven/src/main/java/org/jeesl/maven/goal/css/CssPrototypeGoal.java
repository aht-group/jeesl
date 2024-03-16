package org.jeesl.maven.goal.css;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import net.sf.ahtutils.controller.factory.html.CssPrototypeBuilder;
import net.sf.exlp.util.io.RelativePathFactory;

@Mojo(name="createCssPrototype")
public class CssPrototypeGoal extends AbstractMojo
{    
	@Parameter(defaultValue="${project.build.directory}")
    private String buildDir;
    
	@Parameter(defaultValue="${project.build.directory}/aupCSS.${project.artifactId}")
    private String targetDir;
	
	@Parameter(defaultValue="${project.build.directory}/${project.build.finalName}/resources/css")
    private String resourceDir;
    
	@Parameter(defaultValue="aupCustom.css")
    private String cssName;
    
	@Parameter(defaultValue="#F00014")
    private String colorDark;
    
	@Parameter(defaultValue="#B91212")
    private String colorMedium;
    
	@Parameter(defaultValue="#B90000")
    private String colorLight;
    	
    public void execute() throws MojoExecutionException
    {    	    	
    	File fTmpDir = new File(targetDir);
    	if(!fTmpDir.exists()){fTmpDir.mkdirs();}
    	
    	File fResourceDir = new File(resourceDir);
    	File fBuildDir = new File(buildDir);
    	
    	if(!fBuildDir.exists()){throw new MojoExecutionException(fBuildDir.getAbsolutePath()+" does not exist");}
    	if(!fResourceDir.exists())
    	{
    		fResourceDir.mkdirs();
    	}
    	
    	if(!fResourceDir.exists()){throw new MojoExecutionException(fResourceDir.getAbsolutePath()+" does not exist");}
    	if(!fResourceDir.isDirectory()){throw new MojoExecutionException(fResourceDir.getAbsolutePath()+" is not a directory");}
    	
    	executeCssBuilder(fTmpDir,fResourceDir);
   
    	RelativePathFactory rpf = new RelativePathFactory(fBuildDir);
    	getLog().info("Executing CssBuilder (Prototype): "+rpf.relativate(fResourceDir)+"/"+cssName);
    	try {FileUtils.deleteDirectory(fTmpDir);}
    	catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
    
    private void executeCssBuilder(File fTmpDir, File fResourceDir) throws MojoExecutionException
    {
    	CssPrototypeBuilder cssBuilder = new CssPrototypeBuilder(fTmpDir,fResourceDir,cssName);
    	cssBuilder.buildCss(colorLight,colorMedium,colorDark);
    }
}