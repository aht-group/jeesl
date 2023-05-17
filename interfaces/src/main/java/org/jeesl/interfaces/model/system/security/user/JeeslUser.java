package org.jeesl.interfaces.model.system.security.user;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslUser <R extends JeeslSecurityRole<?,?,?,?,?,?,?>>
		extends Serializable,EjbWithId,EjbPersistable,EjbSaveable,EjbRemoveable
{
	public enum Attributes{firstName,lastName}
	
	String getFirstName();
	void setFirstName(String firstName);
	
	String getLastName();
	void setLastName(String lastName);
	
	String getSalt();
	void setSalt(String salt);
	
	Boolean getPermitLogin();
	void setPermitLogin(Boolean permitLogin);
	
	List<R> getRoles();
	void setRoles(List<R> roles);
}