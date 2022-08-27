package org.jeesl.interfaces.model.system.security.user.pwd;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslUserPasswordSalt extends EjbWithId,JeeslWithPwd
{	
	String getSalt();
	void setSalt(String salt);
}