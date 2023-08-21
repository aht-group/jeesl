package org.jeesl.interfaces.model.system.security.user;

import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;

public interface JeeslUser <R extends JeeslSecurityRole<?,?,?,?,?,?,?>>
					extends EjbPersistable,EjbSaveable,EjbRemoveable,
							JeeslSimpleUser
{
	public enum Attributes{firstName,lastName,roles}
		
	String getSalt();
	void setSalt(String salt);
	
	Boolean getPermitLogin();
	void setPermitLogin(Boolean permitLogin);
	
	List<R> getRoles();
	void setRoles(List<R> roles);
}