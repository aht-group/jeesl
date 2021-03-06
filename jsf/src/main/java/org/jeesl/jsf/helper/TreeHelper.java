package org.jeesl.jsf.helper;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.jeesl.interfaces.controller.handler.Expression;
import org.jeesl.interfaces.controller.handler.Functor;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TreeHelper
{
	final static Logger logger = LoggerFactory.getLogger(TreeHelper.class);
	
	private static TreeNode getAncestor(@NotNull TreeNode decendant, int ancestryLevel)
	{
		TreeNode ancestor = decendant;
		for (int i = 0; i < ancestryLevel; i++)
		{
			ancestor = ancestor.getParent();
			if (ancestor == null) { break; }
		}
		return ancestor;
	}
	
	private static int getDepth(TreeNode root)
	{
		return 1 + root.getChildren().stream().map(child -> getDepth(child)).max(Integer::compare).orElse(0);
	}
	
	public static <T extends EjbWithParentAttributeResolver> void buildTree(JeeslFacade facade, TreeNode parent, List<T> objects, Class<T> type)
	{
		for(T o : objects)
		{
			TreeNode n = new DefaultTreeNode(o, parent);
			
			List<T> children = facade.allForParent(type, o);
			if (!children.isEmpty())
			{
				buildTree(facade, n, children, type);
			}
		}
	}
	
	public static TreeNode findNode(TreeNode node, Expression<TreeNode> expression)
	{
		if (node == null) { return null; }
		
		if (expression.condition(node))
		{
			return node;
		}
		for (TreeNode child : node.getChildren())
		{
			TreeNode n = findNode(child, expression);
			if (n != null)
			{
				return n;
			}
		}
		return null;
	}
	
	public static List<TreeNode> findNodes(TreeNode node, Expression<TreeNode> expression)
	{
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		if (node == null) { return nodes; }
		
		if (expression.condition(node))
		{
			nodes.add(node);
		}
		for (TreeNode child : node.getChildren())
		{
			nodes.addAll(findNodes(child, expression));
		}
		
		return nodes;
	}
	
	public static void forEach(TreeNode node, Functor<TreeNode> functor, Expression<TreeNode> breakExpression)
	{
		if (node == null || breakExpression.condition(node)) { return; }
		
		functor.execute(node);
		node.getChildren().forEach(child -> forEach(child, functor, breakExpression));
	}
	
	public static void setExpansion(TreeNode startNode, boolean expand)
	{
		setExpansion(startNode, expand, getDepth(startNode));
	}
	
	public static void setExpansion(TreeNode startNode, boolean expand, int reach)
	{
		forEach(startNode, node -> node.setExpanded(expand), node -> getAncestor(node, reach) == startNode);
	}
	
	public static TreeNode getNode(TreeNode tree, String dragId, int position)
    {
    	String[] elements = dragId.split(":");
    	String[] index = elements[position].split("_");
    	return getNode(tree.getChildren(),index,0);
    }
    
    private static TreeNode getNode(List<TreeNode> nodes, String[] index, int level)
    {
    	Integer position = Integer.valueOf(index[level]);
    	TreeNode n = nodes.get(position);
    	if(index.length==(level+1)){return n;}
    	else {return getNode(n.getChildren(),index,level+1);}
    }
}
