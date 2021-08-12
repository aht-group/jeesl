package net.sf.ahtutils.prototype.web.component;

import javax.faces.component.FacesComponent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.jeesl.web.component.grid.Grid;

@FacesComponent("net.sf.ahtutils.prototype.web.component.FullHdGrid")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class FullHdGrid extends Grid{
	public FullHdGrid()
	{
		super();
		slot=144;
		gutter=5;
	}

}
