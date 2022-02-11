package org.jeesl.controller.exlp.gc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.jeesl.interfaces.controller.handler.Expression;
import org.jeesl.interfaces.controller.handler.Functor;

public class Tree<T>
{
	public static enum TraversalMode { UP, DOWN, ANY, FULL };
	
	private List<Tree<T>> children = new ArrayList<Tree<T>>();
	/*
	 * Returns an unmodifiable list of all the children attached below this tree node.
	 * 
	 * NOTE: Must be immutable in order to maintain synchronicity between children and parents. Use setParent and/or add/removeChild functions to modify the tree data.
	 * @return An unmodifiable list of Tree nodes attached to this node.
	 */
	public List<Tree<T>> getChildren() { return Collections.unmodifiableList(this.children); }
	
    private Tree<T> parent = null; public Tree<T> getParent() { return this.parent; }
    /**
     * Removes itself as child of the previous parent and adds itself as child of the new one. Adds the passed node as parent.
     * @param parent The node, which is to be the new parent of this node. May be null.
     */
    public void setParent(Tree<T> parent)
    {
    	if (!this.parent.isRoot())
    	{
    		this.parent.children.remove(this);
    	}
    	if (parent != null && !parent.children.contains(this))
    	{
    		parent.children.add(this);
    	}
    	this.parent = parent;
    }
    
    private T data = null; public T getData() { return this.data; } public void setData(T data) { this.data = data; }

    /*
     * Creates a new tree structure with an empty data entry.
     */
    public Tree() { }
    
    /*
     * Creates a new tree structure with this node as root.
     * @param data The data the new node shall represent.
     */
    public Tree(T data)
    {
        this.data = data;
    }

    /*
     * Creates a new tree structure below the passed parent node.
     * @param data The data the new node shall represent.
     * @param parent The parent node this node is to be child of.
     */
    public Tree(T data, @NotNull Tree<T> parent)
    {
        this.data = data;
        parent.addChild(this);
    }

    /*
     * Checks whether this node is the root node of the tree.
     * @return Value indicating whether this node has a parent. True if it does not have a parent, false otherwise.
     */
    public boolean isRoot() 
    {
        return (this.parent == null);
    }

    /*
     * Checks whether this node contains no children.
     * @return Value indicating whether this node has any children. True if at least one child is present, false otherwise.
     */
    public boolean isLeaf()
    {
        return this.children.size() == 0;
    }
    
    /*
     * Adds a new child with the passed data below this tree node.
     * @param data The data to be stored in the node.
     */
    public void addChild(T data)
    {
    	new Tree<T>(data, this);
    }
    
    /*
     * Adds a child below this tree node if not present yet.
     * @param node The child node, which is to be added. Does no changes if the child is already present or is null.
     */
    public void addChild(@NotNull Tree<T> node)
    {
    	if (node == null || this.children.contains(node)) { return; }
    	
    	node.parent = this;
    	this.children.add(node);
    }
    
    /*
     * Adds multiple nodes as child to this tree node.
     * @param dataWithStructure A list of either data elements or Tree elements to be added as children.
     */
    public void addChildren(List<Tree<T>> nodes)
    {
    	nodes.forEach(node -> addChild(node));
    }
    
    /*
     * Removes a child from this tree node if present.
     * @param child The child node, which is to be removed. Does no changes if the child cannot be found or is null.
     */
    public void removeChild(@NotNull Tree<T> child)
    {
    	if (child == null || !this.children.contains(child)) { return; }

    	child.parent = null;
		this.children.remove(child);
    }
    
    /*
     * Removes a number of children from this tree node.
     * @param children A list of child nodes, which are to be removed.
     */
    public void removeChildren(List<Tree<T>> children)
    {
    	children.forEach(child -> removeChild(child));
    }
    
    /*
     * Counts the number of child elements.
     * @param directOnly If true, counts only direct children, otherwise all descendants will be counted.
     * @return The number of children.
     */
    public int countChildren(boolean directOnly)
    {
    	if (directOnly)
    	{
    		return this.children.size();
    	}
    	return this.children.size() + this.children.stream().mapToInt(child -> child.countChildren(directOnly)).sum();
    }
    
    /*
     * Traverses the tree according to the given traversal mode to find every node whose data meets the condition imposed by the given expression.
     * 
     * NOTE: Does not return the tree nodes themselves but the data stored within.
     * @param expression Provides the condition upon which the tree nodes will be selected to return.
     * @param traversalMode Defines how the tree will be iterated and what parts will be included in the search.
     * @return A list containing the data elements meeting the requirement within the range of the search.
     */
    public List<T> find(@NotNull Expression<T> expression, TraversalMode traversalMode)
    {
    	List<T> findResult = new ArrayList<T>();
    	switch (traversalMode)
    	{
    		case UP:
    			if (expression.condition(this.data))
    			{
    				findResult.add(this.data);
    			}
    			if (!isRoot())
    			{
    				findResult.addAll(this.parent.find(expression, TraversalMode.UP));
    			}
    			break;
    		case DOWN:
    			if (expression.condition(this.data))
    			{
    				findResult.add(this.data);
    			}
    			this.children.forEach(child -> findResult.addAll(child.find(expression, TraversalMode.DOWN)));
    			break;
    		case ANY:
    			findResult.addAll(find(expression, TraversalMode.UP));
    			this.children.forEach(child -> findResult.addAll(child.find(expression, TraversalMode.DOWN)));
    			break;
    		case FULL:
	    		Tree<T> startNode = this;
	    		while (!startNode.isRoot())
	    		{
	    			startNode = startNode.parent;
	    		}
	    		return startNode.find(expression, TraversalMode.DOWN);
    	}
    	return findResult;
    }
    
    /*
     * Traverses the tree and executes the passed functor on the data element of each node included in the given traversal mode.
     * @param functor Provides the function, which will be executed on the data element.
     * @param traversalMode Defines how the tree will be iterated and what parts will be included in the execution.
     */
    public void forEach(@NotNull Functor<T> functor, TraversalMode traversalMode)
    {
    	switch(traversalMode)
    	{
    	case UP:
        	functor.execute(this.data);
			if (!isRoot())
			{
				this.parent.forEach(functor, TraversalMode.UP);
			}
    		break;
    	case DOWN:
        	functor.execute(this.data);
    		this.children.forEach(child -> child.forEach(functor, TraversalMode.DOWN));
    		break;
    	case ANY:
    		forEach(functor, TraversalMode.UP);
    		this.children.forEach(child -> child.forEach(functor, TraversalMode.DOWN));
    		break;
    	case FULL:
    		Tree<T> startNode = this;
    		while (!startNode.isRoot())
    		{
    			startNode = startNode.parent;
    		}
    		startNode.forEach(functor, TraversalMode.DOWN);
    		break;
    	}
    }
}
