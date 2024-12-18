package org.jeesl.controller.web.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
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
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityMfa;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityMfaType;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

public class JeeslMfaGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
							CTX extends JeeslSecurityContext<L,D>,
							MFA extends JeeslSecurityMfa<UJ,MFT>,
							MFT extends JeeslSecurityMfaType<L,D,MFT,?>,
							UJ extends JeeslSecurityUser,
							UP extends JeeslUser<?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslMfaGwc.class);
	
	public enum Constraints{qrCodeVerification}
	
	private final SecurityFactoryBuilder<L,D,?,?,?,?,?,?,?,?,?,MFA,MFT,?,?,?,?,UJ,UP> fbSecurity;
	private JeeslSecurityFacade<?,?,?,?,?,?,?,UP> fSecurity;
	
	private final JeeslSecurityMfaCallback callback;
	
	private final EjbSecurityMfaFactory<MFA,MFT,UJ> efMfa;
	
	private final List<MFT> types; public List<MFT> getTypes() {return types;}
	private final List<MFA> mfas; public List<MFA> getMfas() {return mfas;}
	
	private String localeCode;
	private CTX context;
	private UJ user;
	private UP uProject;
	private MFA mfa; public MFA getMfa() {return mfa;} public void setMfa(MFA mfa) {this.mfa = mfa;}

	private String qrCode; public String getQrCode() {return qrCode;}
	private String qrVerification; public String getQrVerification() {return qrVerification;} public void setQrVerification(String qrVerification) {this.qrVerification = qrVerification;}

	public JeeslMfaGwc(JeeslSecurityMfaCallback callback,
						SecurityFactoryBuilder<L,D,?,?,?,?,?,?,?,?,?,MFA,MFT,?,?,?,?,UJ,UP> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.callback=callback;
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
	
	public void reload(CTX context, UJ user, UP uProject, String localeCode)
	{
		logger.info("Reaload for "+user.toString());
		this.context=context;
		this.user=user;
		this.uProject=uProject;
		this.localeCode=localeCode;
		
		this.reladMfas();
	}
	
	private void reset(boolean rMfa, boolean rQr)
	{
		if(rMfa) {mfa=null; callback.callbackMfaConstraintsClear();}
		if(rQr) {qrCode = null;}
	}
	
	private void reladMfas()
	{
		mfas.clear();
		mfas.addAll(fSecurity.allForParent(fbSecurity.getClassMfa(), user));
	}
	
	public void addMfa()
	{
		callback.callbackMfaConstraintsClear();
		mfa = efMfa.build(user,types.get(0));
		
		GoogleAuthenticator authenticator = new GoogleAuthenticator();
		GoogleAuthenticatorKey key = authenticator.createCredentials();
		mfa.setJson(key.getKey());
		
		qrCode = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(context.getName().get(localeCode).getLang(),uProject.getEmail(),key);
	}
	
	public void selectMfa()
	{
		this.reset(false,true);
		mfa = fSecurity.find(fbSecurity.getClassMfa(),mfa);
	}
	
	public void saveMfa() throws JeeslConstraintViolationException, JeeslLockingException
	{
		callback.callbackMfaConstraintsClear();
		efMfa.converter(fSecurity, mfa);
		
		GoogleAuthenticator authenticator = new GoogleAuthenticator();
		boolean codeIsVerified = false;
		
		if(ObjectUtils.isNotEmpty(qrVerification))
		{
			codeIsVerified = authenticator.authorize(mfa.getJson(),Integer.parseInt(qrVerification));
		}
		
		if(!codeIsVerified)
		{
			callback.callbackMfaConstraintsFailedVerification();
			return;
		}
		
		
		logger.info("Matches: "+codeIsVerified);
		
		mfa = fSecurity.save(mfa);
		qrCode=null;
		qrVerification=null;
		this.reladMfas();
	}
}