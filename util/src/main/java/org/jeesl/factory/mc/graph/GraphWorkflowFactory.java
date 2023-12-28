package org.jeesl.factory.mc.graph;

import org.jeesl.model.xml.module.workflow.Stage;
import org.jeesl.model.xml.module.workflow.Transition;
import org.jeesl.util.query.xpath.StatusXpath;
import org.metachart.factory.xml.graph.XmlEdgeFactory;
import org.metachart.model.xml.graph.Edge;
import org.metachart.model.xml.graph.Edges;
import org.metachart.model.xml.graph.Graph;
import org.metachart.model.xml.graph.Node;
import org.metachart.model.xml.graph.Nodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class GraphWorkflowFactory
{
	final static Logger logger = LoggerFactory.getLogger(GraphWorkflowFactory.class);
	
	private final String localeCode;
	
	public GraphWorkflowFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public Graph build(org.jeesl.model.xml.module.workflow.Process process)
	{
		Graph graph = new Graph();
		graph.setNodes(new Nodes());
		graph.setEdges(new Edges());
		
		for(Stage s : process.getStage())
		{
			Node node = new Node();
			node.setId(s.getId());
			node.setCode(s.getId()+"");
			node.setSize(0);
			try {node.setLabel(StatusXpath.getLang(s.getLangs(),localeCode).getTranslation());}
			catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
			
			node.setSizeRelative(true);
			node.setSizeAdjustsColor(true);
			
			graph.getNodes().getNode().add(node);
			
			for(Transition t : s.getTransition())
			{
				Edge edge = XmlEdgeFactory.build(s.getId(), t.getStage().getId(), true);
				try {edge.setLabel(StatusXpath.getLang(t.getLangs(),localeCode).getTranslation());}
				catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
				graph.getEdges().getEdge().add(edge);
			}
		}
		
		return graph;
	}
}