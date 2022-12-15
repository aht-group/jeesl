package org.jeesl.jsf.handler.ui.edit;

import org.jeesl.interfaces.controller.handler.UiEditHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//jeesl.highlight:showcase
public class UiEditBooleanHandler implements UiEditHandler
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UiEditBooleanHandler.class);
	
	private boolean allow;
	
	public boolean isAllow() {return allow;}
	public boolean isDeny() {return !allow;}

	public UiEditBooleanHandler()
	{
		allow = false;
	}
	
	public void toggle()
	{
		allow = !allow;
	}
	
	public void denyEdit() {allow=false;}
	public void allowEdit() {allow=false;}
}
//jeesl.highlight:showcase