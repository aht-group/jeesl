package org.jeesl.interfaces.model.io.logging;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithDateTimeRange;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslIoLog<STATUS extends JeeslIoLogStatus<?,?,STATUS,?>,
								RETENTION extends JeeslIoLogRetention<?,?,RETENTION,?>,
								USER extends JeeslSimpleUser
								>
		extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,
		JeeslWithDateTimeRange
{	
	public static enum Attributes{status,retention,user};

	STATUS getStatus();
	void setStatus(STATUS status);

	RETENTION getRetention();
	void setRetention(RETENTION retention);

	USER getUser();
	void setUser(USER user);
	
}