package org.jeesl.interfaces.model.io.label.revision.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;

public interface JeeslRevisionViewMapping<RV extends JeeslRevisionView<?,?,?>,
									RE extends JeeslRevisionEntity<?,?,?,REM,?,?>,
									REM extends JeeslRevisionEntityMapping<?,?,RE>>
		extends Serializable,EjbPersistable,EjbRemoveable,EjbSaveable,
				EjbWithPositionVisible
{					
	RV getView();
	void setView(RV view);
	
	RE getEntity();
	void setEntity(RE entity);
	
	REM getEntityMapping();
	void setEntityMapping(REM entityMapping);
}