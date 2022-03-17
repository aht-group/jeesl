package org.jeesl.interfaces.model.io.revision.tracker;

import java.util.Date;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslLastUpdate <USER extends JeeslUser<?>>
		extends EjbWithId
{
	public Date getLastUpdateAt();
	public void setLastUpdateAt(Date record);
	
//	public LocalDateTime getLastUpdateAt();
//	public void setLastUpdateAt(LocalDateTime ldt);
	
	public USER getLastUpdateBy();
	public void setLastUpdateBy(USER user);
	
}