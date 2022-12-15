package org.jeesl.jsf.handler.ui.edit;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UiEditSavedHandler <T extends EjbWithId> implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UiEditSavedHandler.class);

	private final Set<T> set;

	private boolean allow; public boolean isAllow() {return allow;}
	private boolean visible = false; public boolean isVisible() {return visible;} public void setVisible(boolean visible) {this.visible = visible;}

	public boolean isDeny() {return !allow;}

	public UiEditSavedHandler()
	{
		set = new HashSet<>();
		this.visible=false;
		reset();
	}

	private void reset()
	{
		set.clear();
		allow = false;
	}

	public void update(T item)
	{
		if(EjbIdFactory.isUnSaved(item)) {allow=true;}
		else
		{
			allow = set.contains(item);
		}
	}

	public void saved(T item)
	{
		set.add(item);
		update(item);
	}
//	
//	public <T extends JeeslWithStatus<S>, S extends JeeslStatus<S,?,?>> void allowForStatus(T ejb, S status);
}