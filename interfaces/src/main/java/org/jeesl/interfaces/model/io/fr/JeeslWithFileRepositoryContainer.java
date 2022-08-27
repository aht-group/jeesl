package org.jeesl.interfaces.model.io.fr;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithFileRepositoryContainer <CONTAINER extends JeeslFileContainer<?,?>> extends EjbWithId
{
	public enum Attributes{frContainer}
	
	CONTAINER getFrContainer();
	void setFrContainer(CONTAINER frContainer);
}