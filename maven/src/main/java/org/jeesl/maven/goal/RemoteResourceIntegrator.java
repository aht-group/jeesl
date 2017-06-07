package org.jeesl.maven.goal;


import java.io.File;
import java.net.URL;
import org.apache.ant.compress.taskdefs.Unzip;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Download and unzip a resource to be integrated in an Installer.
 *
 */
@Mojo( name = "integrateResource")
public class RemoteResourceIntegrator extends AbstractMojo
{   
    /**
     * URL of resource archive.
     */
    @Parameter( property ="integrateResource.resourceArchiveUrl")
    private String resourceArchiveUrl;
    
    /**
     * Should the archive be unpacked?
     */
    @Parameter( property ="integrateResource.unzip", defaultValue = "false")
    private Boolean unzip;
    
    /**
     * Target where it should be placed.
     */
    @Parameter( property = "integrateResource.target", defaultValue="${project.build.directory}\\staging\\")
    private String target;
    
    /**
     * Log Level.
     */
    @Parameter( property ="integrateResource.log", defaultValue="INFO")
    private String log;
	
    public void execute() throws MojoExecutionException
    {
    	BasicConfigurator.configure();
    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel(log));
    	
    	getLog().info("Trying to download remote resource from " +resourceArchiveUrl);
        try {
            String fileName = resourceArchiveUrl.substring(resourceArchiveUrl.lastIndexOf("/")+1, resourceArchiveUrl.length());
            File localFile  = new File(target, fileName);
            if (!localFile.exists())
            {   
                getLog().info("Local file not existent - triggering download.");
                FileUtils.copyURLToFile(new URL(resourceArchiveUrl), localFile);
                
            }
            if (unzip) {
                    getLog().info("Local file is requested to be unzipped. Doing so.");
                    Unzip unzipper = new Unzip();
                    unzipper.setSrc(localFile);
                    unzipper.setDest(new File(target));
                    unzipper.execute();
                }
            
        } catch (Exception e) {
            throw new MojoExecutionException("Could not handle resource from given URL: "+e.getMessage());
        }
    }
}