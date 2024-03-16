package org.jeesl.maven.goal.css;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import net.sf.ahtutils.controller.factory.html.CssGridBuilder;
import net.sf.exlp.util.io.RelativePathFactory;
import net.sf.exlp.util.io.RelativePathFactory.PathSeparator;

@Mojo(name="createCssGrid")
public class CssGridGoal extends AbstractMojo
{    
	@Parameter(defaultValue="${project.build.directory}/security.${project.artifactId}")
    private String targetDir;
	
	@Parameter(defaultValue="${project.build.directory}/classes/META-INF/resources/ahtutilsCss")
    private String resourceDir;
    
	@Parameter(defaultValue="${project.build.directory}/classes/META-INF/resources/ahtutilsCss/testGrid.css")
    private String vcsTest;
    	
    public void execute() throws MojoExecutionException
    {    	
    	File fTmpDir = new File(targetDir);
    	if(!fTmpDir.exists()){fTmpDir.mkdirs();}
    	
    	File fResourceDir = new File(resourceDir);
    	if(!fResourceDir.exists()){throw new MojoExecutionException(fResourceDir.getAbsolutePath()+" does not exist");}
    	if(!fResourceDir.isDirectory()){throw new MojoExecutionException(fResourceDir.getAbsolutePath()+" is not a directory");}
    	
    	File fVcsTestFile = new File(vcsTest);
    	if(!fVcsTestFile.getParentFile().exists()){fVcsTestFile=null;}
    	if(fVcsTestFile!=null && !fVcsTestFile.getParentFile().isDirectory()){fVcsTestFile=null;}
    	
    	executeCssBuilder(fTmpDir,fResourceDir,fVcsTestFile);
   
    	try {FileUtils.deleteDirectory(fTmpDir);}
    	catch (IOException e) {throw new MojoExecutionException(e.getMessage());}
    }
    
    private void executeCssBuilder(File fTmpDir,File fResourceDir,File fVcsTestFile) throws MojoExecutionException
    {
    	getLog().info("Executing CssBuilder (Grid)");
    	CssGridBuilder gridFactory = new CssGridBuilder(fTmpDir,fResourceDir);
    	gridFactory.buildCss();
    	
    	if(fVcsTestFile!=null)
    	{
    		RelativePathFactory rpf = new RelativePathFactory(new File("."),PathSeparator.CURRENT);
        	getLog().info("Saving test-file in "+rpf.relativate(fVcsTestFile));
        	gridFactory.buildVcsTestFile(fVcsTestFile);
    	}
    }
}