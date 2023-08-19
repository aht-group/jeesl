package org.jeesl.interfaces.model.system.security.user;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;

public interface JeeslSimpleUser extends EjbWithId,EjbWithEmail
{
	String getFirstName();
	void setFirstName(String firstName);
	
	String getLastName();
	void setLastName(String lastName);
	
	String getSalt();
	void setSalt(String salt);
	
	Boolean getPermitLogin();
	void setPermitLogin(Boolean permitLogin);
}