package org.jeesl.interfaces.model.module.lf.target.time;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.date.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface JeeslLfTargetTimeElement<L extends JeeslLang,
											TTG extends JeeslLfTargetTimeGroup<L,?>>
						extends Serializable,EjbSaveable,EjbRemoveable,EjbWithName,
								EjbWithId,EjbWithRecord,EjbWithParentAttributeResolver
{
	public enum Attributes{group}

	TTG getGroup();
	void setGroup(TTG group);
}