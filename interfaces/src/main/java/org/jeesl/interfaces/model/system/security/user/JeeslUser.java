package org.jeesl.interfaces.model.system.security.user;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslUser <R extends JeeslSecurityRole<?,?,?,?,?,?,?>
							//,REGISTRATION>,
//							REGISTRATION extends JeeslRegistrationStatus<L,D,USER,?>
>
		extends Serializable,EjbWithId,EjbPersistable,EjbSaveable,EjbRemoveable
{	
	String getFirstName();
	void setFirstName(String firstName);
	
	String getLastName();
	void setLastName(String lastName);
	
	Boolean getPermitLogin();
	void setPermitLogin(Boolean permitLogin);
	
	List<R> getRoles();
	void setRoles(List<R> roles);
	
	String getSalt();
	void setSalt(String salt);
}