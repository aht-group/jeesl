package org.jeesl.interfaces.bean.cache;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslIdCache <T extends EjbWithId>
{
	public T ejb(Long id);
}