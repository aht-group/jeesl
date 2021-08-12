package net.sf.ahtutils.prototype.web.component;

import javax.faces.component.FacesComponent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.jeesl.web.component.grid.Grid;

@FacesComponent("net.sf.ahtutils.prototype.web.component.UhdGrid")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class UhdGrid extends Grid{
	public UhdGrid()
	{
		super();
		slot=280;
		gutter=20;
	}

}
