package org.jeesl.interfaces.model.system.security.util;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslStaff<R extends JeeslSecurityRole<?,?,?,?,?,?,USER>,
							USER extends JeeslUser<R>,
							D1 extends EjbWithId, D2 extends EjbWithId>
			extends Serializable,EjbWithId,EjbPersistable,EjbSaveable,EjbRemoveable
{
	public enum Attributes {role,user,domain};
	
	Integer getPosition();
	void setPosition(Integer position);
	
	R getRole();
	void setRole(R role);
	
	USER getUser();
	void setUser(USER user);
	
	D1 getDomain();
	void setDomain(D1 domain);
	
	D2 getDomain2();
	void setDomain2(D2 domain2);
}