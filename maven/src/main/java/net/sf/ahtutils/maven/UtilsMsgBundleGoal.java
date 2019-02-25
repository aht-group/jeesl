package net.sf.ahtutils.maven;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.msgbundle.TranslationFactory;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.exlp.xml.io.Dir;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jeesl.maven.goal.JeeslMsgGoal;

@Mojo(name="msgBundle",defaultPhase=LifecyclePhase.INITIALIZE)
public class UtilsMsgBundleGoal extends AbstractMojo
{
	@Parameter(defaultValue="${project.groupId}")
    private String groupId;
    
	@Parameter(defaultValue="${project.parent.artifactId}")
    private String projectArtifactId;
    
	@Parameter(defaultValue="${project.artifactId}")
    private String artifactId;
    
	@Parameter(defaultValue="${project.build.directory}")
    private String projectBuildDirectory;
    
	@Parameter(defaultValue="${basedir}/../doc/src/main/resources/msg.${project.parent.artifactId}")
    private String msgSource;
    
	@Parameter(defaultValue="${basedir}/src/main/resources/msg.${project.artifactId}")
    private String targetDir;
    
    @Parameter(defaultValue="translation.xml")
    private String translationXml;
	
    
    public void execute() throws MojoExecutionException
    {
	    	if(translationXml==null){translationXml = "translation.xml";}
	    	
	    	BasicConfigurator.configure();
	    	Logger.getRootLogger().setLevel(Level.ERROR);
	    	 
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
			dir.setName("msg."+projectArtifactId);
			getLog().info("Saving XML summary file to"+fTranslationsXml.getAbsolutePath());
			JaxbUtil.save(fTranslationsXml,dir,true);
			
			tFactory.writeMessageResourceBundles("msg",fTarget);
			for(String s : tFactory.getStats())
			{
				getLog().info(s);
			}
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
		catch (UtilsNotFoundException e) {e.printStackTrace();}
    }
}