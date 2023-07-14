package org.jeesl.maven.goal;

import static com.github.ferstl.depgraph.dependency.NodeResolution.INCLUDED;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jeesl.api.rest.rs.io.JeeslIoMavenRest;
import org.jeesl.factory.json.io.maven.JsonMavenGraphFactory;
import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
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
	
	@Parameter(defaultValue="INFO")
    private String log;

	@Parameter(defaultValue = "${session}", readonly=true)
	private MavenSession mavenSession;
	
	@Parameter(defaultValue = "${project}", readonly=true)
	private MavenProject project;
	
	@Parameter(defaultValue="${project.groupId}")
    private String groupId;
	
	@Parameter(defaultValue="${project.artifactId}")
    private String artifactId;
	
	@Parameter(defaultValue="false")
	private String skip;
	
	private Supplier<Collection<MavenProject>> subProjectsInReactorOrder()
	{
//	    return () -> this.mavenSession.getProjectDependencyGraph().getSortedProjects();
		 return () -> new ArrayList<MavenProject>(Arrays.asList(project));
	}
	
	public void execute() throws MojoExecutionException
	{
		super.getLog().info("Skip: "+skip);
		if(!Boolean.valueOf(skip))
		{
			BasicConfigurator.configure();
			org.apache.log4j.Logger.getRootLogger().setLevel(Level.toLevel(log));
			
			super.getLog().info(project.toString());
			super.getLog().info("groupId: "+groupId);
			super.getLog().info("artifactId: "+artifactId);
	    	
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
		    
		    logger.trace(dependencyGraph);
		      
		    try
		    {
				JsonMavenFerstlGraph ferstl = JsonUtil.read(JsonMavenFerstlGraph.class, dependencyGraph);
				
				JsonMavenGraph graph = JsonMavenGraphFactory.build(ferstl);
				graph.setCode(groupId+":"+artifactId);
				logger.info("Uploading "+graph.getCode());
				
				ResteasyClient client = new ResteasyClientBuilder().build();
//				client.register(new BasicAuthentication(restUser,restPwd));
//				client.register(new RestLogger());
				ResteasyWebTarget target = client.target("https://www.jeesl.org/jeesl");
//				ResteasyWebTarget target = client.target("http://localhost:8080/jeesl");
				JeeslIoMavenRest rest = target.proxy(JeeslIoMavenRest.class);
				JsonSsiUpdate medsage = rest.uploadDependencyGraph(graph);
				JsonUtil.info(medsage);
			}
		    catch (JsonParseException e) {e.printStackTrace();}
		    catch (JsonMappingException e) {e.printStackTrace();}
		    catch (IOException e) {e.printStackTrace();}
		}
	}
}