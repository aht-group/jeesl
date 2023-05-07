package org.jeesl.interfaces.model.with.system.tenant;

import org.jeesl.interfaces.model.system.tenant.JeeslMcsStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithTenantTopic <TOPIC extends JeeslMcsStatus<?,?,?,TOPIC,?>>
	extends EjbWithId
{	
	TOPIC getTopic();
	void setTopic(TOPIC topic);
}