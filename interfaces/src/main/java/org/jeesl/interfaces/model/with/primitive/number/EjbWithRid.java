package org.jeesl.interfaces.model.with.primitive.number;

import java.io.Serializable;

public interface EjbWithRid extends Serializable
{
	long getRid();
	void setRid(long id);
}
