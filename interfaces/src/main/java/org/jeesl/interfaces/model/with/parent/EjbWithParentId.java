package org.jeesl.interfaces.model.with.parent;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithParentId <P extends EjbWithId> extends EjbWithId
{
	P getParent();
	void setParent(P parent);
}