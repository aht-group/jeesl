package net.sf.ahtutils.prototype.web.component;

import javax.faces.component.FacesComponent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.jeesl.web.component.grid.Grid;

@FacesComponent("net.sf.ahtutils.prototype.web.component.HdGrid")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class HdGrid extends Grid
{
	public HdGrid()
	{
		super();
		slot=94;
		gutter=6;
	}
}