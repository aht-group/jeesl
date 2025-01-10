package org.jeesl.controller.web.system.security;

import java.io.Serializable;

import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.system.security.SecurityCategory;
import org.jeesl.model.ejb.system.security.access.SecurityRole;
import org.jeesl.model.ejb.system.security.access.SecurityUsecase;
import org.jeesl.model.ejb.system.security.context.SecurityContext;
import org.jeesl.model.ejb.system.security.page.SecurityAction;
import org.jeesl.model.ejb.system.security.page.SecurityArea;
import org.jeesl.model.ejb.system.security.page.SecurityView;

public class JeeslSecurityRoleWc  <USER extends JeeslUser<SecurityRole>>
									extends JeeslSecurityRoleGwc<IoLang,IoDescription,IoLocale,SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,SecurityContext,SecurityArea,USER>
									implements Serializable
{
	private static final long serialVersionUID = 1L;

	public JeeslSecurityRoleWc(SecurityFactoryBuilder<IoLang,IoDescription,SecurityCategory,SecurityRole,SecurityView,SecurityUsecase,SecurityAction,?,?,?,SecurityArea,?,?,?,?,?,?,?,USER> fbSecurity)
	{
		super(fbSecurity);
	}
}