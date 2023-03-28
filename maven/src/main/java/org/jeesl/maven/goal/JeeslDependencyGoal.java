package org.jeesl.maven.goal;

import static com.github.ferstl.depgraph.dependency.NodeResolution.INCLUDED;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Supplier;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ferstl.depgraph.GraphFormat;
import com.github.ferstl.depgraph.dependency.AggregatingGraphFactory;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.DependencyNodeIdRenderer;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.MavenGraphAdapter;
import com.github.ferstl.depgraph.dependency.json.JsonGraphStyleConfigurer;
import com.github.ferstl.depgraph.graph.GraphBuilder;

@Mojo(name="graph")
public class JeeslDependencyGoal extends AbstractMojo
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDependencyGoal.class);
	
	public JeeslDependencyGoal()
	{
		
	}

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;
	  
	@Parameter(defaultValue = "${session}", readonly = true)
	private MavenSession mavenSession;
	  
	@Component
	ProjectDependenciesResolver dependenciesResolver;
	
	Supplier<Collection<MavenProject>> subProjectsInReactorOrder() {
	    return () -> this.mavenSession.getProjectDependencyGraph().getSortedProjects();
	  }
	
	 public void execute() throws MojoExecutionException
	{
		 BasicConfigurator.configure();
	    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel("DEBUG"));
 	
	    	getLog().info("groupId:");
    	
	    GraphStyleConfigurer graphStyleConfigurer = new JsonGraphStyleConfigurer();
	    	    
	    Path graphFilePath = Paths.get("/Volumes/ramdisk","test.json");

	    ArtifactFilter globalFilter = new AndArtifactFilter();
	    ArtifactFilter transitiveIncludeExcludeFilter = new AndArtifactFilter();
	    ArtifactFilter targetFilter = new AndArtifactFilter();

	    
	    boolean showGroupIds = true;
	    boolean showVersions = true;
	    boolean showTypes = true;
	    boolean showClassifiers = true;
	    
	    DependencyNodeIdRenderer nodeIdRenderer = DependencyNodeIdRenderer.versionlessId()
	            .withClassifier(true)
	            .withType(true)
	            .withScope(true);
	    
	    GraphBuilder<DependencyNode> graphBuilder = graphStyleConfigurer
	            .showGroupIds(true)
	            .showArtifactIds(true)
	            .showTypes(showTypes)
	            .showClassifiers(showClassifiers)
	            .showOptional(true)
	            .showScope(true)
	            .showVersionsOnNodes(true)
	            .showVersionsOnEdges(false)
	            .repeatTransitiveDependencies(false)
	            .configure(GraphBuilder.create(nodeIdRenderer));
	    
	    MavenGraphAdapter adapter = new MavenGraphAdapter(dependenciesResolver, transitiveIncludeExcludeFilter, targetFilter, EnumSet.of(INCLUDED));
	     
	    
	    GraphFactory graphFactory = new AggregatingGraphFactory(adapter, subProjectsInReactorOrder(), globalFilter, graphBuilder,false,true);
	      String dependencyGraph = graphFactory.createGraph(project);
	    
	      logger.info(dependencyGraph);
	}
	
}