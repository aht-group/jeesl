package org.jeesl.interfaces.model.system.security.user;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;

public interface JeeslPasswordUser <R extends JeeslSecurityRole<?,?,?,?,?,?,?>
							//,REGISTRATION>,
//							REGISTRATION extends JeeslRegistrationStatus<L,D,USER,?>
>
		extends JeeslUser<R>
{
	public enum Attributes{pwd}
	String getPwd();
	void setPwd(String pwd);
}