package org.jeesl.interfaces.model.with.parent;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;

public interface EjbWithParentPosition <P extends EjbWithId> extends EjbWithId,EjbWithPosition
{
	P getParent();
	void setParent(P parent);
}