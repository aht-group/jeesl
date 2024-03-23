package org.jeesl.controller.processor.io.maven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenDependencyFactory;
import org.jeesl.factory.txt.io.maven.TxtMavenVersionFactory;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenOutdate;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.jeesl.util.query.ejb.io.maven.EjbIoMavenQuery;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.metachart.factory.json.graph.mc.JsonCategoryFactory;
import org.metachart.factory.json.graph.mc.JsonEdgeFactory;
import org.metachart.factory.json.graph.mc.JsonGraphFactory;
import org.metachart.factory.json.graph.mc.JsonNodeFactory;
import org.metachart.model.json.graph.mc.JsonGraph;
import org.metachart.model.json.graph.mc.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenMetachartGraphProcessor
{
	final static Logger logger = LoggerFactory.getLogger(MavenMetachartGraphProcessor.class);
	
	private final JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage,IoMavenEeReferral> fMaven;
	
	private final Set<IoMavenVersion> dependencyNodes;
	private final Set<IoMavenDependency> dependencyEdges;
	
	@SuppressWarnings("unused")
	private IoMavenVersion root;
	private String localeCode = "en";
	
	public static MavenMetachartGraphProcessor instance(JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage,IoMavenEeReferral> fMaven)
	{
		return new MavenMetachartGraphProcessor(fMaven);
	}
	
	private MavenMetachartGraphProcessor(JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage,IoMavenEeReferral> fMaven)
	{
		this.fMaven=fMaven;
		
		dependencyNodes = new HashSet<>();
		dependencyEdges = new HashSet<>();
	}
	
	public void dependenciesOf(IoMavenVersion root)
	{
		this.root=root;
		dependencyNodes.add(root);
		
		this.resolveDependencies(Arrays.asList(root));
	}
	
	private void resolveDependencies(Collection<IoMavenVersion> versions)
	{
		List<IoMavenDependency> dependencies = fMaven.fIoMavenDependencies(EjbIoMavenQuery.instance().addVersions(versions));
		dependencyEdges.addAll(dependencies);
		
		List<IoMavenVersion> unprocessed = new ArrayList<>();
		for(IoMavenVersion v : EjbMavenDependencyFactory.toListDependsOn(dependencies))
		{
			if(!dependencyNodes.contains(v)) {unprocessed.add(v);}
		}
		dependencyNodes.addAll(unprocessed);
		if(!unprocessed.isEmpty()) {this.resolveDependencies(unprocessed);}
	}
	
	private void resolveDependencies(Collection<IoMavenVersion> roots, Set<IoMavenVersion> nodes, Set<IoMavenDependency> edges)
	{
		List<IoMavenDependency> dependencies = fMaven.fIoMavenDependencies(EjbIoMavenQuery.instance().addVersions(roots));
		
		edges.addAll(dependencies);
		
		List<IoMavenVersion> unprocessed = new ArrayList<>();
		for(IoMavenVersion v : EjbMavenDependencyFactory.toListDependsOn(dependencies))
		{
			if(!nodes.contains(v)) {unprocessed.add(v);}
		}
		nodes.addAll(unprocessed);
		nodes.addAll(roots);
		if(!unprocessed.isEmpty()) {this.resolveDependencies(unprocessed,nodes,edges);}
	}
	
	public JsonGraph toMetachartGraph()
	{
		JsonGraphFactory jf = JsonGraphFactory.instance();
	
		List<IoMavenVersion> nodes = new ArrayList<>(dependencyNodes);
		
		for(IoMavenVersion v : nodes)
		{
			JsonNode jNode = JsonNodeFactory.create();
			jNode.setId(v.getId());
			jNode.setLabel(TxtMavenVersionFactory.full(v));
			jNode.setCategory(JsonCategoryFactory.createId(v.getOutdate().getId()));
			
			jf.node(jNode);
		}
		for(IoMavenDependency d : dependencyEdges)
		{
			jf.edge(JsonEdgeFactory.id(d.getArtifact().getId(),d.getDependsOn().getId()));
		}
		
		List<IoMavenOutdate> outdates = new ArrayList<>();
		outdates.add(fMaven.fByEnum(IoMavenOutdate.class,JeeslMavenOutdate.Code.acceptable));
		outdates.add(fMaven.fByEnum(IoMavenOutdate.class,JeeslMavenOutdate.Code.latest));
		outdates.add(fMaven.fByEnum(IoMavenOutdate.class,JeeslMavenOutdate.Code.outdate));
		outdates.add(fMaven.fByEnum(IoMavenOutdate.class,JeeslMavenOutdate.Code.critical));
		outdates.add(fMaven.fByEnum(IoMavenOutdate.class,JeeslMavenOutdate.Code.unknown));
		
		for(IoMavenOutdate c : outdates)
		{
			jf.category(JsonCategoryFactory.instance().id(c.getId()).label(c.getName().get(localeCode).getLang()).build());
		}
		
		return jf.build();
	}
	
	public void addUsagePath(IoMavenVersion root)
	{
		List<IoMavenUsage> usages = fMaven.fIoMavenUsages(EjbIoMavenQuery.instance().addVersions(Arrays.asList(root)).addRootFetch(JeeslIoMavenUsage.Attributes.scopes));
		logger.info(IoMavenUsage.class.getSimpleName()+" "+usages.size());
		for(IoMavenUsage u : usages)
		{
			try
			{		
				IoMavenGroup group = fMaven.fByCode(IoMavenGroup.class,u.getModule().getCode().split(":")[0]);
				IoMavenArtifact artifact = fMaven.fIoMavenArtifact(group, u.getModule().getCode().split(":")[1]);
				
				List<IoMavenVersion> versions = fMaven.allForParent(IoMavenVersion.class,artifact);
				
				if(!versions.isEmpty())
				{
					IoMavenVersion v = versions.get(0);
					
//					if(v.getArtifact().getCode().equals("exlp-core"))
					{
						logger.info(u.toString()+" "+u.getModule().getCode()+" "+TxtMavenVersionFactory.full(v));
						try
						{
						this.path(v,root);
						}
						catch (IllegalArgumentException e) {}
					}
				}
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}	
	}
	
	private void path(IoMavenVersion source, IoMavenVersion destination)
	{
		logger.info("Path from "+source.getId()+":"+TxtMavenVersionFactory.full(source)+" to "+TxtMavenVersionFactory.full(destination));
		
		Collection<IoMavenVersion> roots = Arrays.asList(source);
		Set<IoMavenVersion> nodes = new HashSet<>();
		Set<IoMavenDependency> edges = new HashSet<>();
		
		this.resolveDependencies(roots, nodes, edges);
		logger.info(IoMavenVersion.class.getSimpleName()+".nodes1: "+nodes.size());
		
		for(IoMavenDependency d : edges)
		{
			nodes.add(d.getArtifact());
			nodes.add(d.getDependsOn());
		}
		logger.info(IoMavenVersion.class.getSimpleName()+".nodes2: "+nodes.size());
		Map<MultiKey<IoMavenVersion>,IoMavenDependency> map = EjbMavenDependencyFactory.toMultiKeyMap(edges);
		
		Graph<IoMavenVersion,DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
		for(IoMavenVersion v : nodes)
		{
			graph.addVertex(v);
		}
		for(IoMavenDependency d : edges)
		{
			graph.addEdge(d.getArtifact(),d.getDependsOn());
		}
			
		DijkstraShortestPath<IoMavenVersion,DefaultEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<IoMavenVersion,DefaultEdge> path = dijkstra.getPath(source, destination);

        if(Objects.nonNull(path))
        {
        	List<IoMavenVersion> list = path.getVertexList();
        	for(int i=1;i<list.size();i++)
        	{
        		IoMavenVersion vStart = list.get(i-1);
        		IoMavenVersion vEnd = list.get(i);
        		dependencyNodes.add(vStart); dependencyNodes.add(vEnd);
        		
        		MultiKey<IoMavenVersion> mk = new MultiKey<IoMavenVersion>(vStart,vEnd);
        		if(map.containsKey(mk)) {dependencyEdges.add(map.get(mk));}
        		
        	}
        	
            for(IoMavenVersion v : path.getVertexList())
            {
            	
            	logger.info(TxtMavenVersionFactory.full(v));
            }
        }
        else {logger.warn("NYI Error");}
	}
}