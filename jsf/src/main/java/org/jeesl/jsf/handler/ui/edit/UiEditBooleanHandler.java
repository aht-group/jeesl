package org.jeesl.jsf.handler.ui.edit;

import org.jeesl.interfaces.controller.handler.UiEditHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//jeesl.highlight:showcase
public class UiEditBooleanHandler implements UiEditHandler
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UiEditBooleanHandler.class);
	
	private boolean allow; public void setAllow(boolean allow) {this.allow = allow;}
	public UiEditBooleanHandler value(boolean value) {this.allow=value; return this;}
	
	public boolean isAllow() {return allow;}
	public boolean isDeny() {return !allow;}

	public static UiEditBooleanHandler instance() {return new UiEditBooleanHandler();}
	public UiEditBooleanHandler()
	{
		allow = false;
	}
	
	public void toggle()
	{
		allow = !allow;
	}
	
	public UiEditBooleanHandler denyEdit() {allow=false; return this;}
	public UiEditBooleanHandler allowEdit() {allow=true; return this;}
	
	public UiEditBooleanHandler setEdit(boolean value)
	{
		this.allow=value;
		return this;
		
	}
}
//jeesl.highlight:showcase