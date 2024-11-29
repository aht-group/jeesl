package org.jeesl.controller.web.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.user.EjbSecurityMfaFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.system.security.JeeslSecurityMfaCallback;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityMfa;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityMfaType;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAccountGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
							UJ extends JeeslSecurityUser,
							UP extends JeeslUser<?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAccountGwc.class);
	
	private UP user;
	public UP getUser() {
		return user;
	}
	public void setUser(UP user) {
		this.user = user;
	}
	protected String pwd0; public String getPwd0() {return pwd0;} public void setPwd0(String pwd0) {this.pwd0 = pwd0;}
	protected String pwd1; public String getPwd1() {return pwd1;} public void setPwd1(String pwd1) {this.pwd1 = pwd1;}
	protected String pwd2; public String getPwd2() {return pwd2;} public void setPwd2(String pwd2){this.pwd2 = pwd2;}
	
	public JeeslAccountGwc(JeeslSecurityMfaCallback callback,
						SecurityFactoryBuilder<L,D,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,UJ,UP> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		
	}
	
	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslSecurityFacade<?,?,?,?,?,?,?,UP> fSecurity,
			UP user)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.user=user;
	}
	
	public boolean validEntries()
	{
		if(pwd0!=null && pwd1!=null && pwd2!=null)
		{
			pwd0 = pwd0.trim();
			pwd1 = pwd1.trim();
			pwd2 = pwd2.trim();
			return true;
		}
		else {return false;}
	}
	

}