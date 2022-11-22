package org.jeesl.jsf.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.controller.handler.Expression;
import org.jeesl.interfaces.controller.handler.Functor;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.util.tree.JeeslTreeElement;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.parent.EjbWithParentId;
import org.primefaces.event.TreeDragDropEvent;
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
    
    // The following functions are from EH Module
    public static <T extends EjbWithParentId<T>> void buildTree(TreeNode treeParent, List<T> list) {buildTree(treeParent,null,list,null);}
    public static <T extends EjbWithParentId<T>> void buildTree(TreeNode treeParent, List<T> list, Set<T> path) {buildTree(treeParent,null,list,path);}
	private static <T extends EjbWithParentId<T>> void buildTree(TreeNode treeParent, T elementParent, List<T> list, Set<T> path)
	{
		List<T> childs = list.stream()
				.filter(t -> (elementParent==null && t.getParent()==null) || (elementParent!=null && t.getParent()!=null && elementParent.getId()==t.getParent().getId()))
				.collect(Collectors.toList());
		list.removeAll(childs);
		
		for(T t : childs)
		{
			TreeNode n = new DefaultTreeNode(t,treeParent);
			if(path!=null && path.contains(t)) {n.setExpanded(true);}
			TreeHelper.buildTree(n,t,list,path);
		}	
	}
    
    @SuppressWarnings("unchecked")
	public static <T extends JeeslTreeElement<T>> void persistDragDropEvent(JeeslFacade facade, TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
    {
    	TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();
        logger.info("Dragged " + dragNode.getData() + " Dropped on " + dropNode.getData() + " at " + dropIndex);
        
        T parent = null;
        if(!(dropNode.getData() instanceof String)){parent = (T)dropNode.getData();}
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
        	T child = (T)n.getData();
    		child.setParent(parent);
    		child.setPosition(index);
    		facade.save(child);
    		index++;
        }
    }
    
    public static <T extends JeeslTreeElement<T>> void fillPath(Set<T> path, T element) throws JeeslConstraintViolationException, JeeslLockingException
    {
    	path.add(element);
    	if(element.getParent()!=null) {fillPath(path,element.getParent());}
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends JeeslTreeElement<T>> TreeNode findNode(TreeNode tree, T ejb) throws JeeslConstraintViolationException, JeeslLockingException
    {
    	for(TreeNode node : tree.getChildren())
    	{
			T t = (T)node.getData();
    		if(t.equals(ejb)) {return node;}
    		else
    		{
    			TreeNode child = findNode(node,ejb);
    			if(child!=null) {return child;}
    		}
    	}
        return null;
    }
}
