package org.jeesl.interfaces.model.system.security.user.identity;

import java.io.Serializable;

public interface JeeslIdentityLogin extends Serializable
{	
	String getLoginName();
	String getLoginPassword();
}