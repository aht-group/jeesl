package org.jeesl.interfaces.model.system.security.user;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithPwd;

public interface JeeslPasswordHistory <USER extends JeeslUser<?>>
			extends Serializable,EjbPersistable,EjbSaveable,EjbRemoveable,
						EjbWithId,EjbWithPwd,EjbWithRecord,EjbWithParentAttributeResolver
{
	public enum Attributes{user}
	
	USER getUser();
	void setUser(USER user);
}