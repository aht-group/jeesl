package org.jeesl.util.query.ejb.system;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.ejb.system.security.SecurityCategory;
import org.jeesl.model.ejb.system.security.access.SecurityRole;
import org.jeesl.model.ejb.system.security.access.SecurityUsecase;
import org.jeesl.model.ejb.system.security.context.SecurityContext;
import org.jeesl.model.ejb.system.security.page.SecurityAction;
import org.jeesl.model.ejb.system.security.page.SecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityQuery <USER extends JeeslUser<SecurityRole>>
					extends EjbSecurityQuery<SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,USER>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(SecurityQuery.class);
	
	public static <USER extends JeeslUser<SecurityRole>> SecurityQuery<USER> instance()
	{
		return new SecurityQuery<>();
	}
}