package net.sf.ahtutils.doc.ofx.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.jeesl.model.xml.system.security.Security;
import org.jeesl.model.xml.system.security.View;
import org.jeesl.util.query.xpath.SecurityXpath;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.model.xml.addon.graph.Node;
import org.openfuxml.model.xml.addon.graph.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxMenuTreeFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxMenuTreeFactory.class);
	
	public OfxMenuTreeFactory(org.exlp.interfaces.system.property.Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxMenuTreeFactory(org.exlp.interfaces.system.property.Configuration config,String[] langs, Translations translations)
	{
		super(config,langs,translations);
	}
	
	public Tree build(Menu menu, Security access) throws OfxAuthoringException, UtilsConfigurationException
	{
		Tree tree = new Tree();
		
		Node root = new Node();
		root.getNode().addAll(build(menu.getMenuItem(),access));
		tree.setNode(root);
		
		return tree;
	}
	
	private List<Node> build(List<MenuItem> items, Security access) throws UtilsConfigurationException
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
	
	private Node build(MenuItem item, Security access) throws UtilsConfigurationException
	{
		try
		{
			Node node = new Node();
			node.setCode(item.getCode());
			
			if(Objects.nonNull(item.getView()))
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