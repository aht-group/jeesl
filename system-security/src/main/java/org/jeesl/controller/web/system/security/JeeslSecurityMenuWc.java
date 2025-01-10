package org.jeesl.controller.web.system.security;

import java.io.Serializable;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.system.security.login.SecurityPasswordRating;
import org.jeesl.model.ejb.system.security.login.SecurityPasswordRule;
import org.jeesl.model.ejb.system.security.user.SecurityPasswordHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSecurityMenuWc extends JeeslPasswordGwc<SecurityPasswordRating,SecurityPasswordRule,SecurityPasswordHistory> implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityMenuWc.class);
	
	public static JeeslSecurityMenuWc instance() {return new JeeslSecurityMenuWc();}
	private JeeslSecurityMenuWc()
	{
		super(SecurityPasswordRating.class);
	}
	
	public JeeslSecurityMenuWc postConstruct(JeeslFacade facade)
	{
		super.genericPostConstruct(facade);
		return this;
	}
}