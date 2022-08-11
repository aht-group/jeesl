package org.jeesl.interfaces.model.io.label.revision.tracker;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.user.EjbWithUser;

public interface JeeslActionTracker <USER extends JeeslUser<?>>
		extends EjbWithId,EjbWithRecord,EjbWithUser<USER>
{
	
}