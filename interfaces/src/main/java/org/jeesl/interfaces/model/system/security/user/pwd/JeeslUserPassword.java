package org.jeesl.interfaces.model.system.security.user.pwd;

import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;

public interface JeeslUserPassword <R extends JeeslSecurityRole<?,?,?,?,?,?,?>> extends JeeslWithPwd,JeeslUser<R>
{
	public enum Attributes{pwd}
}