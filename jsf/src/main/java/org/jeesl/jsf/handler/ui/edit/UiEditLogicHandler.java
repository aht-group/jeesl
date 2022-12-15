package org.jeesl.jsf.handler.ui.edit;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.handler.UiEditHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//jeesl.highlight:showcase
public class UiEditLogicHandler implements UiEditHandler
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UiEditLogicHandler.class);

	public enum Type{AND,OR}

	private final Type type;
	private final List<UiEditHandler> list;
	
	private boolean allow;
	
	public boolean isAllow() {return allow;}
	public boolean isDeny() {return !allow;}
	
	public UiEditLogicHandler(Type type)
	{
		this.type=type;
		list = new ArrayList<>();
	}

	public void addHandler(UiEditHandler h) {list.add(h);}
	
	public void update()
	{
		Boolean result = null;
		if(list.isEmpty()) {result=false;}
		for(UiEditHandler h : list)
		{
			if(list.indexOf(h)==0) {result = h.isAllow();}
			else
			{
				switch(type)
				{
					case AND: result = result && h.isAllow(); break;
					case OR:  result = result || h.isAllow(); break;
				}
			}
		}
		allow = result.booleanValue();
	}
	
}
//jeesl.highlight:showcase