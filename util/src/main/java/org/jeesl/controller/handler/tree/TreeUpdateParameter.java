package org.jeesl.controller.handler.tree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeUpdateParameter implements Cloneable
{
	final static Logger logger = LoggerFactory.getLogger(TreeUpdateParameter.class);
	
	private boolean fillParent;
	public boolean isFillParent() {return fillParent;}
	public void setFillParent(boolean fillParent) {this.fillParent = fillParent;}

	private boolean fillChilds;
	public boolean isFillChilds() {return fillChilds;}
	public void setFillChilds(boolean fillChilds) {this.fillChilds = fillChilds;}
	
	private boolean selectChild;
	public boolean isSelectChild() { return selectChild;}
	public void setSelectChild(boolean selectChild) {this.selectChild = selectChild;}
	
	private boolean callback;
	public boolean isCallback() {return callback;}
	public void setCallback(boolean callback) {this.callback = callback;}
	
//	private boolean fireEvent;
//	public boolean isFireEvent() {return fireEvent;}
//	public void setFireEvent(boolean fireEvent) {this.fireEvent = fireEvent;}
	
	public static TreeUpdateParameter build(boolean fillParent, boolean fillChilds, boolean selectChild, boolean callback)
	{
		TreeUpdateParameter hlup = new TreeUpdateParameter();
		hlup.setFillParent(fillParent);
		hlup.setFillChilds(fillChilds);
		hlup.setSelectChild(selectChild);
		hlup.setCallback(callback);
		return hlup;
	}
	
	public TreeUpdateParameter copy()
	{
		TreeUpdateParameter next = new TreeUpdateParameter();
		next.setFillParent(this.isFillParent());
		next.setFillChilds(this.isFillChilds());
		next.setSelectChild(this.isSelectChild());
		next.setCallback(this.isCallback());
		return next;
	}
	
	public TreeUpdateParameter fillParent(boolean value)
	{
		setFillParent(value);
		return this;
	}
	
	public TreeUpdateParameter selectChild(boolean value)
	{
		setSelectChild(value);
		return this;
	}
	
	public TreeUpdateParameter callback(boolean value)
	{
		setCallback(value);
		return this;
	}
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("fillParent:").append(fillParent);
		sb.append(" fillChilds:").append(fillChilds);
		sb.append(" selectChild:").append(selectChild);
		sb.append(" callback:").append(callback);
		return sb.toString();
	}
}