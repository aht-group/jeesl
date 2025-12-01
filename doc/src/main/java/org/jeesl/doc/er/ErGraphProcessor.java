package org.jeesl.doc.er;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.ClassUtil;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.interfaces.qualifier.er.EjbErParent;
import org.jeesl.model.xml.io.label.Entities;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.util.ReflectionUtil;
import org.jeesl.util.query.xpath.RevisionXpath;
import org.jeesl.util.query.xpath.StatusXpath;
import org.metachart.model.xml.graph.Cluster;
import org.metachart.model.xml.graph.Clusters;
import org.metachart.model.xml.graph.Edge;
import org.metachart.model.xml.graph.Edges;
import org.metachart.model.xml.graph.Graph;
import org.metachart.model.xml.graph.Node;
import org.metachart.model.xml.graph.Nodes;
import org.metachart.processor.graph.ColorSchemeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.io.dir.RecursiveFileFinder;

public class ErGraphProcessor
{
	private static enum Cardinality {OneToOne,OneToMany,ManyToOne,ManyToMany}
	final static Logger logger = LoggerFactory.getLogger(ErGraphProcessor.class);

	private String localeCode;
	private Entities entities;

	private File fBase;

	private Map<String,Node> mapNodes;
	private Map<String,Edge> mapEdges;
	private Map<String,Boolean> mapChilds;
	private Graph graph;
	private ColorSchemeManager csm;
	
	private final Map<String,List<Node>> mapCategoryNodes;

	public ErGraphProcessor(File fBase, ColorSchemeManager csm)
	{
		this.fBase=fBase;
		this.csm = csm;

		mapNodes = new Hashtable<String,Node>();
		mapEdges = new Hashtable<String,Edge>();
		mapChilds = new Hashtable<String,Boolean>();
		mapCategoryNodes = new HashMap<String,List<Node>>();
		graph = new Graph();
		graph.setNodes(new Nodes());
		graph.setEdges(new Edges());
		graph.setClusters(new Clusters());
	}

	public void activateEntities(String localeCode, Entities entities)
	{
		logger.trace("Activating Entites "+localeCode);
		this.localeCode=localeCode;
		this.entities=entities;
	}

	@Deprecated
	public void addPackages(String sEjbPackage) throws IOException, ClassNotFoundException
	{
		this.addPackages(sEjbPackage,new ArrayList<String>());
	}
	
	public void addPackages(Path path, List<String> subset) throws IOException, ClassNotFoundException
	{
		Set<String> setSubset = new HashSet<String>(subset);

		IOFileFilter suffixFileFilter = FileFilterUtils.suffixFileFilter(".java");

		RecursiveFileFinder finder = new RecursiveFileFinder(suffixFileFilter);
		List<File> list = finder.find(path.toFile());

		logger.info("Found files "+list.size()+" in "+path.toFile().getAbsolutePath());
		for(File f : list)
		{
			try
			{
				this.createNode(path,f,setSubset);
			}
			catch (NoClassDefFoundError | ClassNotFoundException e)
			{
				logger.debug("NCDFE or CNFE {}",f.getAbsolutePath());
			}
		}
	}
	
	public void buildGraph()
	{
		for(Node n : mapNodes.values())
		{
			n.setId(Integer.valueOf(graph.getNodes().getNode().size()).longValue());
			graph.getNodes().getNode().add(n);
		}
		
		for(Node n : ListUtils.emptyIfNull(graph.getNodes().getNode()))
		{
			try
			{
				Class<?> c = Class.forName(n.getCode());
				this.createEdge(c);
				this.createHierarchies(c);
			}
			catch (NoClassDefFoundError | ClassNotFoundException e) {}
		}
	}
	
	@Deprecated
	public void addPackages(String sEjbPackage, List<String> subset) throws IOException, ClassNotFoundException
	{
		Set<String> setSub = new HashSet<String>(subset);

		IOFileFilter suffixFileFilter = FileFilterUtils.suffixFileFilter(".java");
		File fPackage = new File(fBase,sEjbPackage);

		RecursiveFileFinder finder = new RecursiveFileFinder(suffixFileFilter);
		List<File> list = finder.find(fPackage);

		logger.info("Found files "+list.size()+" in "+fPackage.getAbsolutePath());

		for(File f : list)
		{
			this.createNode(f,setSub);
		}
		
		long i=0;
		for(String key : mapNodes.keySet())
		{
			Node n = mapNodes.get(key);
			n.setId(i);i++;
			//n.setCode(null);
			//mapNodes.put(key, n);
			graph.getNodes().getNode().add(n);
		}
		for(File f : list)
		{
			createEdge(f);
			createHierarchies(f);
		}
	}

	public Graph create()
	{
		this.mergeEdges();
		this.reGroupMergedNodes();
		this.createClusters();
		return graph;
	}

	@Deprecated
	private void createNode(File f, Set<String> subSet) throws ClassNotFoundException
	{
		logger.info(f.getAbsolutePath());
		Class<?> c = ClassUtil.forFile(fBase, f);
		this.createNode(c,subSet);
	}
	private void createNode(Path pRoot, File f, Set<String> subSet) throws ClassNotFoundException
	{
		logger.trace(f.getAbsolutePath());
		Class<?> c = ClassUtil.forFile(pRoot.toFile(), f);
		this.createNode(c,subSet);
	}
	private void createNode(Class<?> c, Set<String> subSet)
	{
		logger.trace("Testing {}",c.getSimpleName());
		Annotation a = c.getAnnotation(EjbErNode.class);
		if(a!=null)
		{
			EjbErNode er = (EjbErNode)a;
			Node node = new Node();
			node.setCode(c.getName());

			if(localeCode!=null && entities!=null)
			{
				try
				{
					Entity entity = RevisionXpath.getEntity(entities,node.getCode());
					node.setLabel(StatusXpath.getLang(entity.getLangs(),localeCode).getTranslation());
//					logger.error("node.Label: "+node.getLabel());
				}
				catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e){/*logger.warn(e.getMessage());*/}
			}
			if(node.getLabel()==null)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("No Translation of ").append(er.name());
				sb.append(" localeCode:").append(localeCode);
				if(entities==null) {sb.append(" entites:null");}
				else {sb.append(" entties:").append(entities.getEntity().size());}
				sb.append(" ").append(c.getName());
				logger.debug(sb.toString());
				node.setLabel("** "+er.name());
			}

			if(er.category().length()>0){node.setCategory(er.category());}

			node.setSizeRelative(true);
			node.setSizeAdjustsColor(true);
			node.setSize(1-er.level());
			node.setType(""+er.level());

			boolean add = false;
			if(subSet.isEmpty()){add=true;}
			else
			{
				logger.trace("Subset available");
				String[] items = er.subset().split(",");
				for(String s : items)
				{
					if(subSet.contains(s))
					{
						if(node.getLabel().startsWith("**"))
						{
							logger.error("No Translation "+c.getName());
						}	
						add=true;
					}
				}
			}
			if(mapNodes.containsKey(node.getCode())) {add=false;}
			logger.trace("Node {} in {}",node.getCode(),mapNodes.size());

			if(add)
			{
				logger.info(c.getName());
				mapNodes.put(node.getCode(), node);
				this.addNodeToCategory(node);
			}
		}
	}

	private void createEdge(File fClass) throws ClassNotFoundException
	{
		Class<?> c = ClassUtil.forFile(fBase, fClass);
		this.createEdge(c);
	}
	private void createEdge(Class<?> c) throws ClassNotFoundException
	{
		if(mapNodes.containsKey(c.getName()))
		{
			logger.trace("----------------Processing edges for "+c.getName() + "---------------------");
			Node source = mapNodes.get(c.getName());

			for(Field field : ReflectionUtil.toFields(c))
			{
				logger.trace("Field "+field.getName());
				Annotation annotations[] = field.getAnnotations();
				Cardinality cardinality = getCardinality(annotations);
				logger.trace("Cardinality: "+cardinality);
				if(cardinality!=null)
				{
					if(mapNodes.containsKey(field.getType().getName()))
					{
						Node target = mapNodes.get(field.getType().getName());
						logger.trace("target: " + target.getLabel());
						createEdge(source, cardinality,target,false);
					}
					else {
						String fieldTypeName = field.getType().getName();
						if(fieldTypeName.equals(List.class.getName()) || fieldTypeName.equals(Map.class.getName()))
						{
							ParameterizedType pT = (ParameterizedType) field.getGenericType();
							Class<?> gC = null;
							if(fieldTypeName.equals(Map.class.getName())){
								gC = (Class<?>) pT.getActualTypeArguments()[1];
							}
							else {
								gC = (Class<?>) pT.getActualTypeArguments()[0];
							}
							if(mapNodes.containsKey(gC.getName()))
							{
								Node target = mapNodes.get(gC.getName());
						        logger.trace("target: " + target.getLabel());
						        createEdge(source, cardinality,target,false);
						    }
						}
					}
				}
			}

			Class<?> cSuper = c.getSuperclass();
			if(mapNodes.containsKey(cSuper.getName()))
			{
				Node target = mapNodes.get(cSuper.getName());
				createEdge(source, Cardinality.OneToOne,target,false);
			}
		}
	}

	private void createHierarchies (File fClass) throws ClassNotFoundException
	{
		Class<?> c = ClassUtil.forFile(fBase, fClass);
		this.createHierarchies(c);
	}
	private void createHierarchies (Class<?> c) throws ClassNotFoundException
	{
		Annotation pa = c.getAnnotation(EjbErParent.class);
		if(pa!= null)
		{
			Node source = mapNodes.get(c.getName());
			EjbErParent erParent = (EjbErParent)pa;
			String parentCode = erParent.value().getName();
			Node parentNode = mapNodes.get(parentCode);
			if(parentNode!=null) {createEdge(source, Cardinality.ManyToOne,parentNode,false);}
		}
	}

	private void createEdge(Node source, Cardinality cardinality,Node target,boolean targetIsChild)
	{
		Edge e = new Edge();
		switch(cardinality)
		{
			case OneToOne:  e.setDirected(false);break;
			case OneToMany: e.setDirected(true);break;
			case ManyToOne: e.setDirected(true);break;
			case ManyToMany:e.setDirected(true);break;
		}

		e.setFrom(source.getId());
		e.setTo(target.getId());
		e.setType(cardinality.toString());
		String key = e.getFrom()+"-"+e.getTo();

		mapEdges.put(key, e);
		mapChilds.put(key, targetIsChild);
	}

	private Cardinality getCardinality(Annotation annotations[])
	{
		logger.trace("Annotation Length "+annotations.length);
		Cardinality cardinality = null;
		for (int j = 0; j < annotations.length; j++)
		{
			Annotation a = annotations[j];
			logger.trace(a.annotationType().getName());
			if(a.annotationType().getName().equals(javax.persistence.OneToOne.class.getName())){cardinality = Cardinality.OneToOne;}
			if(a.annotationType().getName().equals(javax.persistence.OneToMany.class.getName())){cardinality = Cardinality.OneToMany;}
			if(a.annotationType().getName().equals(javax.persistence.ManyToOne.class.getName())){cardinality = Cardinality.ManyToOne;}
			if(a.annotationType().getName().equals(javax.persistence.ManyToMany.class.getName())){cardinality = Cardinality.ManyToMany;}
			if(cardinality!=null){return cardinality;}
		}
		return cardinality;
	}

	private void mergeEdges()
	{
		Object[] keys = mapEdges.keySet().toArray();
		for(Object o : keys)
		{
			String keyF = (String)o;
			if(mapEdges.containsKey(keyF))
			{
				Edge eF = mapEdges.get(keyF);
				Cardinality cF = Cardinality.valueOf(eF.getType());
				String keyR = eF.getTo()+"-"+eF.getFrom();
				boolean isChildF = mapChilds.get(keyF);
				if(mapEdges.containsKey(keyR))
				{
					boolean isChildR = mapChilds.get(keyR);
					Edge eR = mapEdges.get(keyR);

					Cardinality cR = Cardinality.valueOf(eR.getType());
					boolean rmF = false;
					boolean rmR = false;
					if(cF==Cardinality.OneToOne && cR == Cardinality.OneToOne){rmR=true;}
					else if(cF==Cardinality.ManyToMany && cR == Cardinality.ManyToMany){rmR=true;}
					else if(cF==Cardinality.OneToMany && cR == Cardinality.ManyToOne){rmR=true;}
					else if(cF==Cardinality.ManyToOne && cR == Cardinality.OneToMany){rmF=true;}

					if(rmF) {mapEdges.remove(keyF);}
					else if(rmR && !isChildR) {mapEdges.remove(keyR);}
				}
				else
				{
					if(!isChildF && cF==Cardinality.ManyToOne)
					{
						long from = eF.getFrom();
						long to = eF.getTo();
						eF.setTo(from);
						eF.setFrom(to);
						eF.setDirected(false);
					}
				}
			}
		}
		graph.getEdges().getEdge().addAll(mapEdges.values());
	}

	private void reGroupMergedNodes()
	{
		Set<String> categories = mapCategoryNodes.keySet();
		
		Map<Node,String> mergedNodes = new HashMap<Node,String>();

		for(String category : categories)
		{
			List<Node> regroupNodes= csm.getMergeNodesForCluster(category);
			for (Node regroupNode : regroupNodes)
			{
				mergedNodes.put(regroupNode, category);
			}
		}

		for(Map.Entry<String,List<Node>> entry : mapCategoryNodes.entrySet())
		{
			String category = entry.getKey();
			List<Node> categoryNodes = entry.getValue();
			List<Node> categoryRemoveList = new ArrayList<Node>();
			List<Node> mergedNodeRemoveList = new ArrayList<Node>();

			for(Map.Entry<Node,String>  mergedNodeEntry : mergedNodes.entrySet())
			{
				Node mergeNode = mergedNodeEntry.getKey();
				String mergeToCategory = mergedNodeEntry.getValue();
				int index = this.getIndexOf(categoryNodes, mergeNode);
				if(index >=0)
				{
					logger.info("Move node : " + categoryNodes.get(index).getLabel() +" from category :" + category + " to category: " + mergeToCategory);
					categoryRemoveList.add(categoryNodes.get(index));
					mergeNodeToCategory(categoryNodes.get(index), mergeToCategory);
					mergedNodeRemoveList.add(mergeNode);
					}
			}
			mapCategoryNodes.get(category).removeAll(categoryRemoveList);

			for (Node mergedNodeRemove : mergedNodeRemoveList)
			{
				mergedNodes.remove(mergedNodeRemove);
			}
		}

		logger.info("Regroup finished");
	}
	
	public void createClusters()
	{
		//logger.trace("---" + mapNodesCategories.keySet().toString() +"---");
		int nodeCategoryId = 0;
		for(Map.Entry<String,List<Node>> entry : mapCategoryNodes.entrySet())
		{
			boolean skipCatagorization  = false;
			if(entry.getKey() == "NA" || entry.getValue().size() < 2) {skipCatagorization = true;}
			if(!skipCatagorization)
			{
				//logger.trace("--" + entry.getKey() +"--");
				Cluster cluster  = new Cluster();
				cluster.setCode(Integer.toString(nodeCategoryId));
				cluster.setLabel(entry.getKey());

				for(Node n: entry.getValue())
				{
					Node clusterNode= new Node();
					clusterNode.setId(n.getId());
					cluster.getNode().add(clusterNode);
				}
				graph.getClusters().getCluster().add(cluster);
			}
			nodeCategoryId++;
		}
	}

	private void mergeNodeToCategory(Node node, String category)
	{
		mapCategoryNodes.get(category).add(node);
	}

	private int getIndexOf(List<Node> categoryNodes, Node node)
	{
		for (int i = 0; i < categoryNodes.size(); i++)
		{
			try
			{
				 if (categoryNodes.get(i).getCode().contains(node.getCode())) {return i;}
			}
			catch (NullPointerException e) {logger.error("Node name:" + categoryNodes.get(i).getLabel() +" code: " +  categoryNodes.get(i).getCode());}
		}
		return -1;
	}

	private void addNodeToCategory(Node node)
	{
		String category = ObjectUtils.isNotEmpty(node.getCategory()) ? node.getCategory() : "NA";
		if(!mapCategoryNodes.containsKey(category)) {mapCategoryNodes.put(category,new ArrayList<>());}
		mapCategoryNodes.get(category).add(node);
	}
}