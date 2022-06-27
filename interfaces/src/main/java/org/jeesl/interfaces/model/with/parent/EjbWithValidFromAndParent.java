package org.jeesl.interfaces.model.with.parent;

import org.jeesl.interfaces.model.with.date.ju.EjbWithValidFrom;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithValidFromAndParent extends EjbWithId,EjbWithValidFrom,EjbWithParentAttributeResolver
{
	
}