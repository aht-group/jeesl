package org.jeesl.interfaces.model.io.label.revision.data;

import java.util.Date;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslLastUpdate <USER extends JeeslUser<?>> extends EjbWithId
{
	public Date getLastUpdateAt();
	public void setLastUpdateAt(Date lastUpdateAt);
	
	public USER getLastUpdateBy();
	public void setLastUpdateBy(USER lastUpdateBy);
}