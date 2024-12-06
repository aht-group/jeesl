package org.jeesl.controller.web.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.msg.JeeslConstraintsBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.txt.system.security.TxtUserFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.system.security.JeeslSecurityMfaCallback;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAccountGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
							C extends JeeslConstraint<L,D,?,?,?,?>,
							UJ extends JeeslSecurityUser,
							UP extends JeeslUser<?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAccountGwc.class);
	
	public enum Constraint {newPasswordsNotMatching,oldPasswordNotMatching,somePwdFieldsEmpty,pwdNotComplicantWithRules,pwdChanged}
	
	private JeeslConstraintsBean<C> bConstraint;
	
	private final List<C> constraints; public List<C> getConstraints() {return constraints;}

	private UP user; public UP getUser() {return user;} public void setUser(UP user) {this.user = user;}
	
	protected String pwd0; public String getPwd0() {return pwd0;} public void setPwd0(String pwd0) {this.pwd0 = pwd0;}
	protected String pwd1; public String getPwd1() {return pwd1;} public void setPwd1(String pwd1) {this.pwd1 = pwd1;}
	protected String pwd2; public String getPwd2() {return pwd2;} public void setPwd2(String pwd2){this.pwd2 = pwd2;}
	
	public JeeslAccountGwc(JeeslSecurityMfaCallback callback,
						SecurityFactoryBuilder<L,D,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,UJ,UP> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		
		constraints = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslConstraintsBean<C> bConstraint,
			UP user)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.bConstraint=bConstraint;
		this.user=user;
		this.clear();
	}
	
	public void clear()
	{
		constraints.clear();
	}
	
	public boolean passwordFieldsFilled()
	{
		if(ObjectUtils.allNotNull(pwd0,pwd1,pwd2))
		{
			pwd0 = pwd0.trim();
			pwd1 = pwd1.trim();
			pwd2 = pwd2.trim();
			return true;
		}
		else {return false;}
	}
	
	public void verifyAllPasswordFieldsAreFilled()
	{
		if(ObjectUtils.allNotNull(pwd0,pwd1,pwd2))
		{
			pwd0 = pwd0.trim();
			pwd1 = pwd1.trim();
			pwd2 = pwd2.trim();
			
			if(ObjectUtils.isNotEmpty(pwd0) && ObjectUtils.isNotEmpty(pwd1) && ObjectUtils.isNotEmpty(pwd2))
			{
				return;
			}
		}
		
		constraints.add(bConstraint.getSilent(this.getClass(),Constraint.somePwdFieldsEmpty));
	}
	
	public void compareNewPasswords() {this.compareNewPasswords(constraints);}
	public void compareNewPasswords(List<C> constraints)
	{
		if(!pwd1.equals(pwd2)) {constraints.add(bConstraint.getSilent(this.getClass(),Constraint.newPasswordsNotMatching));}
	}
	
	public void verifyOldPasswordMatchesDb() {this.verifyOldPasswordMatchesDb(constraints);}
	public void verifyOldPasswordMatchesDb(List<C> constraints)
	{
		if(!TxtUserFactory.toHash(pwd0,user.getSalt()).equals(user.getPwd())) {constraints.add(bConstraint.getSilent(this.getClass(),Constraint.oldPasswordNotMatching));}
	}
}