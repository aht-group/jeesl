package net.sf.ahtutils.doc.ofx.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.jeesl.util.query.xpath.SecurityXpath;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.content.graph.Node;
import org.openfuxml.content.graph.Tree;
import org.openfuxml.exception.OfxAuthoringException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.xml.access.Access;
import net.sf.ahtutils.xml.security.View;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxMenuTreeFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxMenuTreeFactory.class);
		
	public OfxMenuTreeFactory(Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxMenuTreeFactory(Configuration config,String[] langs, Translations translations)
	{
		super(config,langs,translations);
	}
	
	public Tree build(Menu menu,Access access) throws OfxAuthoringException, UtilsConfigurationException
	{
		Tree tree = new Tree();
		
		Node root = new Node();
		root.getNode().addAll(build(menu.getMenuItem(),access));
		tree.setNode(root);
		
		return tree;
	}
	
	private List<Node> build(List<MenuItem> items,Access access) throws UtilsConfigurationException
	{
		List<Node> nodes = new ArrayList<Node>();
		for(MenuItem item : items)
		{
			Node node = build(item,access);
			node.getNode().addAll(build(item.getMenuItem(),access));
			nodes.add(node);
		}
		return nodes;
	}
	
	private Node build(MenuItem item,Access access) throws UtilsConfigurationException
	{
		try
		{
			Node node = new Node();
			node.setCode(item.getCode());
			
			if(item.isSetView())
			{
				View view = SecurityXpath.getMenuItem(access,item.getView().getCode());
				if(langs.length>1){logger.warn("Incorrect Assignment");}
				node.setLabel(StatusXpath.getLang(view.getLangs(),langs[0]).getTranslation());
			}
			
			return node;
		}
		catch (ExlpXpathNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new UtilsConfigurationException(e.getMessage());}	
	}
}