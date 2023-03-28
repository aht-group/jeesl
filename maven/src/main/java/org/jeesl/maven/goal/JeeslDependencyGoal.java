package org.jeesl.maven.goal;

import static com.github.ferstl.depgraph.dependency.NodeResolution.INCLUDED;

import java.io.IOException;
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
import org.jeesl.factory.json.io.maven.JsonMavenGraphFactory;
import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.ssi.maven.JsonMavenFerstlGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.ferstl.depgraph.dependency.AggregatingGraphFactory;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.DependencyNodeIdRenderer;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.MavenGraphAdapter;
import com.github.ferstl.depgraph.dependency.json.JsonGraphStyleConfigurer;
import com.github.ferstl.depgraph.graph.GraphBuilder;

import net.sf.exlp.util.io.JsonUtil;

@Mojo(name="dependency")
public class JeeslDependencyGoal extends AbstractMojo
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDependencyGoal.class);
	
	@Component ProjectDependenciesResolver dependenciesResolver;
	
	public JeeslDependencyGoal()
	{
		
	}

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;
	  
	@Parameter(defaultValue = "${session}", readonly = true)
	private MavenSession mavenSession;
	
	private Supplier<Collection<MavenProject>> subProjectsInReactorOrder()
	{
	    return () -> this.mavenSession.getProjectDependencyGraph().getSortedProjects();
	}
	
	 public void execute() throws MojoExecutionException
	{
		 BasicConfigurator.configure();
	    	org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel("DEBUG"));
 	
	    	getLog().info("groupId:");
    	
	    GraphStyleConfigurer graphStyleConfigurer = new JsonGraphStyleConfigurer();

	    ArtifactFilter globalFilter = new AndArtifactFilter();
	    ArtifactFilter transitiveIncludeExcludeFilter = new AndArtifactFilter();
	    ArtifactFilter targetFilter = new AndArtifactFilter();
	    
	    DependencyNodeIdRenderer nodeIdRenderer = DependencyNodeIdRenderer.versionlessId()
	            .withClassifier(true)
	            .withType(true)
	            .withScope(true);
	    
	    GraphBuilder<DependencyNode> graphBuilder = graphStyleConfigurer
	            .showGroupIds(true)
	            .showArtifactIds(true)
	            .showTypes(true)
	            .showClassifiers(true)
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
	      
	    try
	    {
			JsonMavenFerstlGraph ferstl = JsonUtil.read(JsonMavenFerstlGraph.class, dependencyGraph);
			JsonUtil.info(ferstl);
			
			JsonMavenGraph graph = JsonMavenGraphFactory.build(ferstl);
			JsonUtil.info(graph);
		}
	    catch (JsonParseException e) {e.printStackTrace();}
	    catch (JsonMappingException e) {e.printStackTrace();}
	    catch (IOException e) {e.printStackTrace();}
	      
	}
}