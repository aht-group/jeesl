package org.jeesl.controller.web.system.security;

import java.io.Serializable;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.system.security.login.SecurityPasswordRating;
import org.jeesl.model.ejb.system.security.login.SecurityPasswordRule;
import org.jeesl.model.ejb.system.security.user.SecurityPasswordHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslPasswordWc extends JeeslPasswordGwc<SecurityPasswordRating,SecurityPasswordRule,SecurityPasswordHistory> implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslPasswordWc.class);
	
	public static JeeslPasswordWc instance() {return new JeeslPasswordWc();}
	private JeeslPasswordWc()
	{
		super(SecurityPasswordRating.class);
	}
	
	public JeeslPasswordWc postConstruct(JeeslFacade facade)
	{
		super.genericPostConstruct(facade);
		return this;
	}
}