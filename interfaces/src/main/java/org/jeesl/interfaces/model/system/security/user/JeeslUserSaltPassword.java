package org.jeesl.interfaces.model.system.security.user;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslUserSaltPassword extends EjbWithId
{
	String getPwd();
	void setPwd(String pwd);
	
	String getSalt();
	void setSalt(String salt);
}