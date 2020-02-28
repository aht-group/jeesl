package org.jeesl.interfaces.model.module.map;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;

public interface JeeslStatisticalMapImplementation<MAP extends JeeslStatisticalMap<?,?>,
													STATUS extends JeeslStatisticMapStatus<?,?,STATUS,?>,
													LEVEL extends JeeslLocationLevel<?,?,LEVEL,?>>
						extends Serializable,EjbSaveable,
								EjbWithId,EjbWithParentAttributeResolver,
								JeeslWithStatus<STATUS>
{	
	public enum Attributes{map}
	
	MAP getMap();
	void setMap(MAP map);
	
	LEVEL getLevel();
	void setLevel(LEVEL level);
}