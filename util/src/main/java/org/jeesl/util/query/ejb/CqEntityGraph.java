package org.jeesl.util.query.ejb;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.db.CqFetch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CqEntityGraph 
{
	final static Logger logger = LoggerFactory.getLogger(CqEntityGraph.class);
	
	public static <T extends EjbWithId> EntityGraph<T> build(EntityManager em, Class<T> c, List<CqFetch> fetches)
	{
		logger.info("Preparing "+EntityGraph.class.getSimpleName());
		CqFetch cqRoot = CqFetch.root(CqFetch.class);
		Map<CqFetch,Set<CqFetch>> mapNode = CqFetch.toMapNode(fetches);
		Map<CqFetch,Set<CqFetch>> mapAttribute = CqFetch.toMapAttribute(fetches);
		
		EntityGraph<T> graph = em.createEntityGraph(c);
		
		if(mapAttribute.containsKey(cqRoot))
		{
			logger.info("Root Attributes");
			for(CqFetch f : mapAttribute.get(cqRoot))
			{
				logger.info("Add root Attribute: "+f.getAttribute());
				graph.addAttributeNodes(f.getAttribute());
			}
		}
		if(mapNode.containsKey(cqRoot))
		{
			for(CqFetch f : mapNode.get(cqRoot))
			{
				logger.info("Adding Sub to Root");
				Subgraph<Object> subgraph = graph.addSubgraph(f.getAttribute());
				CqEntityGraph.subGraph(subgraph, f, mapNode, mapAttribute);
			}
		}
		
		graph.toString();
		
		return graph;
	}
	
	public static <T extends EjbWithId> void subGraph(Subgraph<Object> sub, CqFetch fetch, Map<CqFetch,Set<CqFetch>> mapNode, Map<CqFetch,Set<CqFetch>> mapAttribute)
	{
		if(mapAttribute.containsKey(fetch))
		{
			for(CqFetch f : mapAttribute.get(fetch))
			{
				logger.info("Adding Attribute to sub");
				sub.addAttributeNodes(f.getAttribute());
			}
		}
		if(mapNode.containsKey(fetch))
		{
			for(CqFetch f : mapNode.get(fetch))
			{
				logger.info("Adding Sub to Sub");
				Subgraph<Object> subgraph = sub.addSubgraph(f.getAttribute());
				CqEntityGraph.subGraph(subgraph, f, mapNode, mapAttribute);
			}
		}
	}

}