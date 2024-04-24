package org.jeesl.interfaces.model.with.system.graphic;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithGraphic<G extends JeeslGraphic<?,?,?>> extends EjbWithId
{
	public enum Att {graphic}
	
	G getGraphic();
	void setGraphic(G graphic) ;
}