package org.jeesl.jsf.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.mutable.MutableInt;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.controller.handler.Expression;
import org.jeesl.interfaces.controller.handler.Functor;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.util.tree.JeeslTreeElement;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JeeslTreeHelper <T extends JeeslTreeElement<T>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslTreeHelper.class);
		
	public static <T extends JeeslTreeElement<T>> JeeslTreeHelper<T> instance() {return new JeeslTreeHelper<>();}
	private JeeslTreeHelper()
	{

	}
	
	public TreeNode<T> build(List<T> list)
	{
		TreeNode<T> root = new DefaultTreeNode<>(null, null);
		Map<T,TreeNode<T>> map = new HashMap<>();
		 
		 List<T> copy = new ArrayList<>(list);
		 ListIterator<T> iterator = copy.listIterator();
		 while(iterator.hasNext())
		 {
			 T next = iterator.next();
		     if(Objects.isNull(next.getParent()))
		     {
		    	 TreeNode<T> n = new DefaultTreeNode<>(next,root);
		    	 map.put(next,n);
		    	 iterator.remove();
		     }
		 }
		 logger.info("After: "+copy.size());
		 logger.info("Map: "+map.size());
		 
		 MutableInt idx = new MutableInt(0);
		 while(copy.size()>0)
		 {
			 logger.info("Iteration "+idx.incrementAndGet()+" list:"+copy.size());
			 
			 this.build(copy,map);
		 }
		 logger.info("Complete: "+map.size());
		 
		 return root;
	}
	
	private void build(List<T> list, Map<T,TreeNode<T>> map)
	{
		List<T> handled = new ArrayList<>();
		for(T t : list)
		{
			if(map.containsKey(t.getParent()))
			{
				TreeNode<T> n = new DefaultTreeNode<>(t,map.get(t.getParent()));
				map.put(t,n);
				handled.add(t);
			}
		}
		list.removeAll(handled);
	}
	
	@SuppressWarnings("unchecked")
	public void persistDragDropEvent(JeeslFacade facade, TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
    {
    	TreeNode<T> dragNode = event.getDragNode();
        TreeNode<T> dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();
        logger.info("Dragged " + dragNode.getData() + " Dropped on " + dropNode.getData() + " at " + dropIndex);
        
        int index=1;
        for(TreeNode<T> n : dropNode.getChildren())
        {
        	logger.info(index+" "+n.getData().getId());
        	T child = n.getData();
    		child.setParent(dropNode.getData());
    		child.setPosition(index);
    		facade.save(child);
    		index++;
        }
    }
	
	private int getDepth(TreeNode<T> root)
	{
		return 1 + root.getChildren().stream().map(child -> getDepth(child)).max(Integer::compare).orElse(0);
	}
	
	public void expand(TreeNode<T> tree, TreeNode<T> node)
	{
		this.setExpansion(node!=null ? node : tree, true);
	}
	
	public void setExpansion(TreeNode<T> startNode, boolean expand)
	{
		setExpansion(startNode, expand, getDepth(startNode));
	}
	public void setExpansion(TreeNode<T> startNode, boolean expand, int reach)
	{
		forEach(startNode, node -> node.setExpanded(expand), node -> getAncestor(node, reach) == startNode);
	}
	private void forEach(TreeNode<T> node, Functor<TreeNode<T>> functor, Expression<TreeNode<T>> breakExpression)
	{
		if (node == null || breakExpression.condition(node)) { return; }
		
		functor.execute(node);
		node.getChildren().forEach(child -> forEach(child, functor, breakExpression));
	}
	private TreeNode<T> getAncestor(@NotNull TreeNode<T> decendant, int ancestryLevel)
	{
		TreeNode<T> ancestor = decendant;
		for (int i = 0; i < ancestryLevel; i++)
		{
			ancestor = ancestor.getParent();
			if (ancestor == null) { break; }
		}
		return ancestor;
	}
	
	

	public TreeNode<T> getNode(TreeNode<T> tree, String dragId, int position)
    {
    	String[] elements = dragId.split(":");
    	String[] index = elements[position].split("_");
    	return getNode(tree.getChildren(),index,0);
    }

    private TreeNode<T> getNode(List<TreeNode<T>> nodes, String[] index, int level)
    {
    	Integer position = Integer.valueOf(index[level]);
    	TreeNode<T> n = nodes.get(position);
    	if(index.length==(level+1)){return n;}
    	else {return getNode(n.getChildren(),index,level+1);}
    }
}