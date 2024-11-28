package org.jeesl.controller.web.system.security;

import java.io.Serializable;

import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.controller.web.system.security.JeeslSecurityMfaCallback;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.system.security.login.SecurityMfa;
import org.jeesl.model.ejb.system.security.login.SecurityMfaType;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMfaWc <UP extends JeeslUser<?>>
							extends JeeslMfaGwc<IoLang,IoDescription,IoLocale,SecurityMfa,SecurityMfaType,SecurityUser,UP>
							implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslMfaWc.class);
		
	public JeeslMfaWc(JeeslSecurityMfaCallback callback,
						SecurityFactoryBuilder<IoLang,IoDescription,?,?,?,?,?,?,?,?,?,SecurityMfa,SecurityMfaType,?,?,?,?,SecurityUser,UP> fbSecurity)
	{
		super(callback,fbSecurity);
	}
}