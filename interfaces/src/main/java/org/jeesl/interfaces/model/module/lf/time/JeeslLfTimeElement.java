package org.jeesl.interfaces.model.module.lf.time;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface JeeslLfTimeElement<L extends JeeslLang,
											TG extends JeeslLfTimeGroup<L,?>>
						extends Serializable,EjbSaveable,EjbRemoveable,EjbWithName,
								EjbWithId,EjbWithRecord,EjbWithParentAttributeResolver
{
	public enum Attributes{group}

	TG getGroup();
	void setGroup(TG group);
}