package org.jeesl.interfaces.model.module.ts.data;

import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslTsValue extends EjbWithId,EjbWithRecord
{
	Double getValue();
	void setValue(Double value);
}