package org.jeesl.interfaces.model.module.attribute;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithAttributeContainer <CONTAINER extends JeeslAttributeContainer<?,?>>
		extends EjbWithId
{
	public static String jpaAttribute = "attributeContainer";
	
	CONTAINER getAttributeContainer();
	void setAttributeContainer(CONTAINER attributeContainer);
}