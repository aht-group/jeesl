package org.jeesl.interfaces.model.with.system.status;

import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithPhase<PHASE extends JeeslStatus<?,?,PHASE>> extends EjbWithId
{
	PHASE getPhase();
	void setPhase(PHASE category);
}