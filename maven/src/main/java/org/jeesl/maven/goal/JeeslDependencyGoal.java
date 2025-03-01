package org.jeesl.maven.goal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Supplier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

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
import org.exlp.util.io.JsonUtil;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jeesl.api.rest.rs.jx.io.JeeslIoMavenRest;
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
import com.github.ferstl.depgraph.dependency.NodeResolution;
import com.github.ferstl.depgraph.dependency.json.JsonGraphStyleConfigurer;
import com.github.ferstl.depgraph.graph.GraphBuilder;

@Mojo(name="dependency")
public class JeeslDependencyGoal extends AbstractMojo
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDependencyGoal.class);
	
	@Component ProjectDependenciesResolver dependenciesResolver;
	
	public JeeslDependencyGoal()
	{
		
	}
	
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
		return () -> new ArrayList<MavenProject>(Arrays.asList(project));
	}
	
	public void execute() throws MojoExecutionException
	{
		logger.info("Skip: "+skip);
		if(!Boolean.valueOf(skip))
		{			
			logger.info(project.toString());
			logger.info("groupId: "+groupId);
			logger.info("artifactId: "+artifactId);
	    	
		    GraphStyleConfigurer graphStyleConfigurer = new JsonGraphStyleConfigurer();

		    ArtifactFilter fiGlobal = new AndArtifactFilter();
		    ArtifactFilter fiTransitive = new AndArtifactFilter();
		    ArtifactFilter fiTarget = new AndArtifactFilter();
		    
		    DependencyNodeIdRenderer nodeRenderer = DependencyNodeIdRenderer.versionlessId()
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
		            .repeatTransitiveDependencies(true)
		            .configure(GraphBuilder.create(nodeRenderer));
		    
		    
		    MavenGraphAdapter adapter = new MavenGraphAdapter(dependenciesResolver, fiTransitive, fiTarget, EnumSet.of(NodeResolution.INCLUDED));
		    
		    GraphFactory graphFactory = new AggregatingGraphFactory(adapter,this.subProjectsInReactorOrder(),
		    								fiGlobal, graphBuilder,true,true);
		    String dependencyGraph = graphFactory.createGraph(project);
		    
//		    logger.info("Text debug"); logger.info(dependencyGraph);
		      
		    try
		    {
				JsonMavenFerstlGraph ferstl = JsonUtil.read(JsonMavenFerstlGraph.class, dependencyGraph);
				
				JsonMavenGraph graph = JsonMavenGraphFactory.build(ferstl);
				graph.setCode(groupId+":"+artifactId);
				logger.info("Uploading "+graph.getCode());
				
				Path path = Paths.get("/Volumes/ramdisk");
				if(Files.exists(path) && Files.isDirectory(path))
				{
					Files.write(Paths.get(path.toString(),"graph.txt"), Arrays.asList(dependencyGraph), StandardCharsets.UTF_8);
					JsonUtil.write(graph,new File(path.toFile(),"graph.json"));
				}
				
				Client client = ClientBuilder.newBuilder().build();
//				client.register(new BasicAuthentication(restUser,restPwd));
//				client.register(new RestLogger());
				ResteasyWebTarget target = (ResteasyWebTarget)client.target("https://www.jeesl.org/jeesl");
				
//				ResteasyWebTarget target = client.target("http://localhost:8080/jeesl");
				JeeslIoMavenRest rest = target.proxy(JeeslIoMavenRest.class);
				JsonSsiUpdate message = rest.uploadDependencyGraph(graph);				
				
				if(Objects.nonNull(message.getStatistic()) && Objects.nonNull(message.getStatistic().getError()) && message.getStatistic().getError()>0)
				{
					JsonUtil.info(message);
					throw new MojoExecutionException("Module not configured in JEESL: "+graph.getCode());
				}
			}
		    catch (JsonParseException e) {e.printStackTrace();}
		    catch (JsonMappingException e) {e.printStackTrace();}
		    catch (IOException e) {e.printStackTrace();}
		}
	}
}