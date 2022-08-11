package org.jeesl.interfaces.model.io.label.revision.envers;

import java.io.Serializable;

import org.hibernate.envers.RevisionType;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslRevisionContainer <REV extends JeeslRevision,
										T extends EjbWithId,
										USER extends JeeslUser<?>>
					extends Serializable
{					
	REV getInfo();
	
	RevisionType getType();
	
	USER getUser();
	void setUser(USER user);
	
	T getEntity();
}