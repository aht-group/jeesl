package org.jeesl.interfaces.model.io.domain;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslDomain<L extends JeeslLang,
									ENTITY extends JeeslRevisionEntity<L,?,?,?,?,?>
									>
			extends Serializable,EjbWithId,EjbSaveable,
					EjbWithPosition,EjbWithLang<L>//,EjbWithDescription<D>
{
	ENTITY getEntity();
	void setEntity(ENTITY entiy);
}