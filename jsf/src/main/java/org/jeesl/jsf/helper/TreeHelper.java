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

public final class TreeHelper <P extends EjbWithParentId<P>>
{
	final static Logger logger = LoggerFactory.getLogger(TreeHelper.class);
	
	public static <P extends EjbWithParentId<P>> TreeHelper<P> instance() {return new TreeHelper<>();}
	private TreeHelper()
	{

	}
	
	@Deprecated
	public static <T extends EjbWithParentAttributeResolver> void buildTree(JeeslFacade facade, TreeNode<T> parent, List<T> objects, Class<T> type)
	{
		for(T o : objects)
		{
			TreeNode<T> n = new DefaultTreeNode<>(o, parent);
			
			List<T> children = facade.allForParent(type, o);
			if (!children.isEmpty())
			{
				buildTree(facade, n, children, type);
			}
		}
	}

	public List<TreeNode<P>> findNodes(TreeNode<P> node, Expression<TreeNode<P>> expression)
	{
		List<TreeNode<P>> nodes = new ArrayList<TreeNode<P>>();
		if (node == null) { return nodes; }
		
		if (expression.condition(node))
		{
			nodes.add(node);
		}
		for (TreeNode<P> child : node.getChildren())
		{
			nodes.addAll(findNodes(child, expression));
		}
		
		return nodes;
	}
	
	@Deprecated
	private void forEach(TreeNode<P> node, Functor<TreeNode<P>> functor, Expression<TreeNode<P>> breakExpression)
	{
		if (node == null || breakExpression.condition(node)) { return; }
		
		functor.execute(node);
		node.getChildren().forEach(child -> forEach(child, functor, breakExpression));
	}
	
	@Deprecated
	public void setExpansion(TreeNode<P> startNode, boolean expand)
	{
		setExpansion(startNode, expand, getDepth(startNode));
	}
	@Deprecated private int getDepth(TreeNode<P> root)
	{
		return 1 + root.getChildren().stream().map(child -> getDepth(child)).max(Integer::compare).orElse(0);
	}
	@Deprecated
	public void setExpansion(TreeNode<P> startNode, boolean expand, int reach)
	{
		forEach(startNode, node -> node.setExpanded(expand), node -> getAncestor(node, reach) == startNode);
	}
	
	@Deprecated
	private TreeNode<P> getAncestor(@NotNull TreeNode<P> decendant, int ancestryLevel)
	{
		TreeNode<P> ancestor = decendant;
		for (int i = 0; i < ancestryLevel; i++)
		{
			ancestor = ancestor.getParent();
			if (ancestor == null) { break; }
		}
		return ancestor;
	}
	
	@Deprecated
	public TreeNode<P> getNode(TreeNode<P> tree, String dragId, int position)
    {
    	String[] elements = dragId.split(":");
    	String[] index = elements[position].split("_");
    	return getNode(tree.getChildren(),index,0);
    }
    
	@Deprecated
    private TreeNode<P> getNode(List<TreeNode<P>> nodes, String[] index, int level)
    {
    	Integer position = Integer.valueOf(index[level]);
    	TreeNode<P> n = nodes.get(position);
    	if(index.length==(level+1)){return n;}
    	else {return getNode(n.getChildren(),index,level+1);}
    }
    
    // The following functions are from EH Module
    public static <T extends EjbWithParentId<T>> void buildTree(TreeNode<T> treeParent, List<T> list) {buildTree(treeParent,list,null);}
    public static <T extends EjbWithParentId<T>> void buildTree(TreeNode<T> treeParent, List<T> list, Set<T> path)
    {
    	buildTree(treeParent,null,list,path);
    	if(!list.isEmpty())
    	{
    		logger.info("Size "+list.size());
    		for(T t : list)
    		{
    			new DefaultTreeNode<>(t,treeParent);
    		}
    	}
    }
	private static <T extends EjbWithParentId<T>> void buildTree(TreeNode<T> treeParent, T elementParent, List<T> list, Set<T> path)
	{
		List<T> childs = list.stream()
				.filter(t -> (elementParent==null && t.getParent()==null) || (elementParent!=null && t.getParent()!=null && elementParent.getId()==t.getParent().getId()))
				.collect(Collectors.toList());
		list.removeAll(childs);
		
		for(T t : childs)
		{
			TreeNode<T> n = new DefaultTreeNode<>(t,treeParent);
			if(path!=null && path.contains(t)) {n.setExpanded(true);}
			TreeHelper.buildTree(n,t,list,path);
		}
	}
    
	@Deprecated
    @SuppressWarnings("unchecked")
	public static <T extends JeeslTreeElement<T>> void persistDragDropEvent(JeeslFacade facade, TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
    {
    	TreeNode<T> dragNode = event.getDragNode();
        TreeNode<T> dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();
        logger.info("Dragged " + dragNode.getData() + " Dropped on " + dropNode.getData() + " at " + dropIndex);
        
        T parent = null;
       parent = (T)dropNode.getData();
        int index=1;
        for(TreeNode<T> n : dropNode.getChildren())
        {
        	T child = (T)n.getData();
    		child.setParent(parent);
    		child.setPosition(index);
    		facade.save(child);
    		index++;
        }
    }
    
    public static <T extends JeeslTreeElement<T>> void fillPath(Set<T> path, T element)
    {
    	path.add(element);
    	if(element.getParent()!=null) {fillPath(path,element.getParent());}
    }
    
    public static <T extends JeeslTreeElement<T>> TreeNode<T> findNode(TreeNode<T> tree, T ejb)
    {
    	for(TreeNode<T> node : tree.getChildren())
    	{
			T t = (T)node.getData();
    		if(t.equals(ejb)) {return node;}
    		else
    		{
    			TreeNode<T> child = findNode(node,ejb);
    			if(child!=null) {return child;}
    		}
    	}
        return null;
    }
}