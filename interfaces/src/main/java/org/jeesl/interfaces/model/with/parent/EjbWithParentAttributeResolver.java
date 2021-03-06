package org.jeesl.interfaces.model.with.parent;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithParentAttributeResolver extends EjbWithId
{

	/**
	 * Define the parent attribute
	 * @return the property name of the parent (e.g. district, province)
	 */
	public String resolveParentAttribute();
}