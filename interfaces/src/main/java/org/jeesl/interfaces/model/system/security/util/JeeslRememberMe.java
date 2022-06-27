package org.jeesl.interfaces.model.system.security.util;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.date.ju.EjbWithValidUntil;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.user.EjbWithUser;

public interface JeeslRememberMe <USER extends JeeslUser<?>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
				EjbWithCode,EjbWithValidUntil,
				EjbWithUser<USER>
{	

}