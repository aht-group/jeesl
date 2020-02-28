package org.jeesl.interfaces.model.system.io.dms;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisibleParent;

public interface JeeslIoDmsLayer<VIEW extends JeeslIoDmsView<?,?,?>,
								AI extends JeeslAttributeItem<?,?>>
					extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,
							EjbWithPositionVisibleParent
							
{	
	public enum Attributes{view}
	
	VIEW getView();
	void setView(VIEW view);
}