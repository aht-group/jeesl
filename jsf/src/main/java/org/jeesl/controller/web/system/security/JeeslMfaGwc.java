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

public class JeeslMfaGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
							MFA extends JeeslSecurityMfa<UJ,MFT>,
							MFT extends JeeslSecurityMfaType<L,D,MFT,?>,
							UJ extends JeeslSecurityUser,
							UP extends JeeslUser<?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslMfaGwc.class);
	
	private final SecurityFactoryBuilder<L,D,?,?,?,?,?,?,?,?,?,MFA,MFT,?,?,?,?,UJ,UP> fbSecurity;
	private JeeslSecurityFacade<?,?,?,?,?,?,?,UP> fSecurity;
		
	private final EjbSecurityMfaFactory<MFA,MFT,UJ> efMfa;
	
	private final List<MFT> types; public List<MFT> getTypes() {return types;}
	private final List<MFA> mfas; public List<MFA> getMfas() {return mfas;}
	
	private UJ user;
	private MFA mfa; public MFA getMfa() {return mfa;} public void setMfa(MFA mfa) {this.mfa = mfa;}

	public JeeslMfaGwc(JeeslSecurityMfaCallback callback,
						SecurityFactoryBuilder<L,D,?,?,?,?,?,?,?,?,?,MFA,MFT,?,?,?,?,UJ,UP> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;
		
		efMfa = fbSecurity.ejbMfa();
		
		types = new ArrayList<>();
		mfas = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslSecurityFacade<?,?,?,?,?,?,?,UP> fSecurity)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fSecurity=fSecurity;
		
		types.addAll(fSecurity.allOrderedPositionVisible(fbSecurity.getClassMfaType()));
	}
	
	public void reload(UJ user)
	{
		logger.info("Reaload for "+user.toString());
		this.user=user;
		this.reladMfas();
	}
	
	private void reladMfas()
	{
		mfas.clear();
		mfas.addAll(fSecurity.allForParent(fbSecurity.getClassMfa(), user));
	}
	
	public void addMfa()
	{
		mfa = efMfa.build(user,types.get(0));
	}
	
	public void saveMfa() throws JeeslConstraintViolationException, JeeslLockingException
	{
		efMfa.converter(fSecurity, mfa);
		mfa = fSecurity.save(mfa);
		this.reladMfas();
	}
}